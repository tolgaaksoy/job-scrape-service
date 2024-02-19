package io.jobmarks.jobscrapeservice.linkedin.service;

import io.jobmarks.jobscrapeservice.linkedin.model.*;
import io.jobmarks.jobscrapeservice.linkedin.repository.LinkedInJobPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LinkedInScrapeService {

    private final LinkedInFeignClientAdapter feignClientAdapter;
    private final LinkedInJobPostRepository linkedInJobPostRepository;

    public LinkedInScrapeService(LinkedInFeignClientAdapter feignClientAdapter,
                                 LinkedInJobPostRepository linkedInJobPostRepository) {
        this.feignClientAdapter = feignClientAdapter;
        this.linkedInJobPostRepository = linkedInJobPostRepository;
    }

    public JobResponse scrape(LinkedInFilter filter) {
        List<LinkedInJobPost> jobList = new ArrayList<>();
        while ((filter.getPage() == null ? 0 : filter.getPage()) * 25 < filter.getLimit()) {
            LinkedInJobsDocument linkedInJobsDocument = fetchJobsDocument(filter);
            if (linkedInJobsDocument != null) {
                processJobCards(jobList, linkedInJobsDocument);
            }
            filter.nextPage();
        }
        logJobList(jobList);
        return new JobResponse(jobList);
    }

    private LinkedInJobsDocument fetchJobsDocument(LinkedInFilter filter) {
        try {
            return feignClientAdapter.getJobs(filter);
        } catch (Exception e) {
            log.error("Exception while fetching jobs: {}", e.getMessage(), e);
            return null;
        }
    }

    private void processJobCards(List<LinkedInJobPost> jobList, LinkedInJobsDocument linkedInJobsDocument) {
        for (LinkedinJobCardElement jobCard : linkedInJobsDocument.getLinkedInJobCardElements()) {
            if (!linkedInJobPostRepository.existsByExternalId(jobCard.getExternalId())) {
                LinkedInJobPost jobPost = createJobPost(jobCard);
                jobList.add(jobPost);
            }
        }
    }

    private LinkedInJobPost createJobPost(LinkedinJobCardElement jobCard) {
        LinkedInJobPost.LinkedInJobPostBuilder jobPostBuilder = LinkedInJobPost.builder()
                .externalId(jobCard.getExternalId())
                .title(jobCard.getTitle())
                .companyName(jobCard.getCompanyName())
                .companyUrl(jobCard.getCompanyUrl())
                .location(jobCard.getLocation())
                .datePosted(jobCard.getDatePosted())
                .jobUrl(jobCard.getJobUrl())
                .compensation(jobCard.getCompensation())
                .benefits(jobCard.getBenefits());
        try {
            LinkedInJobDescriptionDocument jobDescription = feignClientAdapter.getJob(jobCard.getExternalId());
            jobPostBuilder
                    .description(jobDescription.getDescription())
                    .applicantsCount(jobDescription.getApplicantsCount())
                    .jobFunction(jobDescription.getJobFunction())
                    .industry(jobDescription.getIndustries())
                    .seniority(jobDescription.getSeniorityLevel())
                    .jobType(jobDescription.getJobType());
        } catch (Exception e) {
            log.error("Exception while fetching job description: {}", e.getMessage(), e);
        }
        LinkedInJobPost jobPost = jobPostBuilder.build();
        linkedInJobPostRepository.save(jobPost);
        return jobPost;
    }

    private void logJobList(List<LinkedInJobPost> jobList) {
        StringBuilder jobs = new StringBuilder();
        for (LinkedInJobPost job : jobList) {
            jobs.append("--------------\n").append(job).append("\n");
        }
        log.info("Jobs:\n{}", jobs);
    }
}