package io.jobmarks.jobscrapeservice.linkedin.service;

import io.jobmarks.jobscrapeservice.common.exception.AccessDeniedException;
import io.jobmarks.jobscrapeservice.linkedin.model.LinkedInFilter;
import io.jobmarks.jobscrapeservice.linkedin.model.LinkedInJobDescriptionDocument;
import io.jobmarks.jobscrapeservice.linkedin.model.LinkedInJobsDocument;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
public class LinkedInClientAdapter {

    private static final int DEFAULT_DELAY_MS = 3000;
    private static final int RANDOM_DELAY_MS = 2000;
    private static final int SECONDS_IN_HOUR = 3600;
    private static final int JOBS_PER_PAGE = 25;

    private final Random random = new Random();

    private final LinkedinRestTemplateClient linkedinRestTemplateClient;

    public LinkedInClientAdapter(LinkedinRestTemplateClient linkedinRestTemplateClient) {
        this.linkedinRestTemplateClient = linkedinRestTemplateClient;
    }

    public LinkedInJobsDocument getJobs(LinkedInFilter filter) {
        try {
            String response = fetchJobsResponse(filter);
            Document document = Jsoup.parse(response);
            return new LinkedInJobsDocument(document);
        } catch (Exception ex) {
            log.error("Failed to get jobs from LinkedIn: {}", ex.getMessage());
            throw new AccessDeniedException("Failed to get jobs from LinkedIn: " + ex.getMessage(), ex);
        }
    }

    public LinkedInJobDescriptionDocument getJob(String externalId) {
        try {
            String response = fetchJobDetailsResponse(externalId);
            Document document = Jsoup.parse(response);
            return new LinkedInJobDescriptionDocument(document);
        } catch (Exception ex) {
            log.error("Failed to get job details from LinkedIn for job ID {}: {}", externalId, ex.getMessage());
            throw new AccessDeniedException("Failed to get job details from LinkedIn for job ID " + externalId + ": " + ex.getMessage(), ex);
        }
    }

    private String fetchJobsResponse(LinkedInFilter filter) {
        String hoursOld = filter.getHoursOld() != null ? String.valueOf(filter.getHoursOld() * SECONDS_IN_HOUR) : "r";
        Integer page = filter.getPage();
        Integer start = page != null ? page * JOBS_PER_PAGE : null;

        String response = linkedinRestTemplateClient.searchJobs(
                filter.getSearchTerm(),
                filter.getLocation(),
                filter.getRemote(),
                filter.getJobType(),
                filter.getEasyApply(),
                filter.getLinkedinCompanyIds(),
                hoursOld,
                page,
                start
        );
        applyRandomDelay();
        return response;
    }

    private String fetchJobDetailsResponse(String externalId) {
        applyRandomDelay();
        return linkedinRestTemplateClient.fetchJobDetails(externalId);
    }

    private void applyRandomDelay() {
        try {
            Thread.sleep((long) DEFAULT_DELAY_MS + random.nextInt(RANDOM_DELAY_MS));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}