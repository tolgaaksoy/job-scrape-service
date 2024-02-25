package io.jobmarks.jobscrapeservice.indeed.service;

import io.jobmarks.jobscrapeservice.indeed.model.*;
import io.jobmarks.jobscrapeservice.indeed.repository.IndeedJobPostsRepository;
import io.jobmarks.jobscrapeservice.linkedin.model.JobResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class IndeedScrapeService {
    private static final int JOBS_PER_PAGE = 25;
    private final IndeedJobPostsRepository indeedJobPostsRepository;
    private final IndeedClientAdapter indeedClientAdapter;

    public IndeedScrapeService(IndeedJobPostsRepository indeedJobPostsRepository,
                               IndeedClientAdapter indeedClientAdapter) {
        this.indeedJobPostsRepository = indeedJobPostsRepository;
        this.indeedClientAdapter = indeedClientAdapter;
    }

    public JobResponse scrape(final IndeedFilter filter) {
        List<IndeedJobPost> jobList = new ArrayList<>();
        while (getCurrentOffset(filter) < filter.getLimit()) {
            Optional<IndeedJobsDocument> indeedJobsDocument = fetchJobsDocument(filter);
            indeedJobsDocument.ifPresent(document -> jobList.addAll(processJobCards(document)));
            filter.nextPage();
        }
        return JobResponse.builder().indeedJobPosts(jobList).build();
    }

    private int getCurrentOffset(IndeedFilter filter) {
        return (filter.getPage() == null ? 0 : filter.getPage()) * JOBS_PER_PAGE;
    }

    private Optional<IndeedJobsDocument> fetchJobsDocument(final IndeedFilter filter) {
        try {
            return Optional.ofNullable(indeedClientAdapter.getJobs(filter));
        } catch (Exception e) {
            log.error("Exception while fetching jobs: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    private List<IndeedJobPost> processJobCards(final IndeedJobsDocument indeedJobsDocument) {
        List<String> externalIds = indeedJobsDocument.getIndeedJobCardElements().stream()
                .map(IndeedJobCardElement::getExternalId)
                .distinct()
                .filter(externalId -> !indeedJobPostsRepository.existsByExternalId(externalId))
                .toList();

        List<IndeedJobDescriptionDocument> details = indeedClientAdapter.getJobDetails(externalIds);

        return indeedJobsDocument.getIndeedJobCardElements().stream()
                .filter(jobCard -> externalIds.contains(jobCard.getExternalId()))
                .map(jobCard -> createJobPost(jobCard, findDetailsForJob(details, jobCard.getExternalId())))
                .toList();
    }

    private Optional<IndeedJobDescriptionDocument> findDetailsForJob(List<IndeedJobDescriptionDocument> details, String externalId) {
        return details.stream()
                .filter(jobDescription -> jobDescription.getExternalId().equals(externalId))
                .findFirst();
    }

    private IndeedJobPost createJobPost(final IndeedJobCardElement jobCard, Optional<IndeedJobDescriptionDocument> jobDescriptionDocument) {
        IndeedJobPost.IndeedJobPostBuilder jobPostBuilder = IndeedJobPost.builder()
                .externalId(jobCard.getExternalId())
                .title(jobCard.getTitle())
                .companyName(jobCard.getCompanyName())
                .datePosted(jobCard.getDatePosted())
                .jobUrl(jobCard.getJobUrl())
                .benefits(jobCard.getBenefits())
                .jobType(jobCard.getJobType());

        jobDescriptionDocument.ifPresent(descriptionDocument -> jobPostBuilder
                .companyUrl(descriptionDocument.getCompanyUrl())
                .description(descriptionDocument.getDescription())
                .location(descriptionDocument.getLocation())
                .industry(descriptionDocument.getIndustry())
                .seniority(descriptionDocument.getExperienceLevel())
                .jobFunction(descriptionDocument.getJobFunction())
                .compensation(descriptionDocument.getCompensation()));

        IndeedJobPost jobPost = jobPostBuilder.build();
        indeedJobPostsRepository.save(jobPost);
        return jobPost;
    }
}