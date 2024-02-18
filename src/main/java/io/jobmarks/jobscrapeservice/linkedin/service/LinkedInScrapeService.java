package io.jobmarks.jobscrapeservice.linkedin.service;


import io.jobmarks.jobscrapeservice.linkedin.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LinkedInScrapeService {

    private final LinkedInFeignClientAdapter feignClientAdapter;

    public LinkedInScrapeService(LinkedInFeignClientAdapter feignClientAdapter) {
        this.feignClientAdapter = feignClientAdapter;
    }

    public JobResponse scrape(LinkedInFilter filter) {
        List<LinkedInJobPost> jobList = new ArrayList<>();
        while (jobList.size() < filter.getLimit()) {
            LinkedInJobsDocument linkedInJobsDocument = fetchJobsDocument(filter);
            if (linkedInJobsDocument != null) {
                processJobCards(jobList, linkedInJobsDocument, filter);
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

    private void processJobCards(List<LinkedInJobPost> jobList, LinkedInJobsDocument linkedInJobsDocument, LinkedInFilter filter) {
        for (LinkedinJobCardElement jobCard : linkedInJobsDocument.getLinkedInJobCardElements()) {
            LinkedInJobPost jobPost = createJobPost(jobCard);
            jobList.add(jobPost);
        }
    }

    private LinkedInJobPost createJobPost(LinkedinJobCardElement jobCard) {
        LinkedInJobPost.LinkedInJobPostBuilder jobPostBuilder = LinkedInJobPost.builder()
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
            jobPostBuilder.description(jobDescription.getDescription())
                    .jobType(jobDescription.getJobType());
        } catch (Exception e) {
            log.error("Exception while fetching job description: {}", e.getMessage(), e);
        }
        return jobPostBuilder.build();
    }

    private void logJobList(List<LinkedInJobPost> jobList) {
        StringBuilder jobs = new StringBuilder();
        for (LinkedInJobPost job : jobList) {
            jobs.append("--------------\n").append(job).append("\n");
        }
        log.info("Jobs:\n{}", jobs);
    }
}
