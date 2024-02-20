package io.jobmarks.jobscrapeservice.linkedin.service;

import feign.FeignException;
import io.jobmarks.jobscrapeservice.common.exception.AccessDeniedException;
import io.jobmarks.jobscrapeservice.linkedin.model.LinkedInFilter;
import io.jobmarks.jobscrapeservice.linkedin.model.LinkedInJobDescriptionDocument;
import io.jobmarks.jobscrapeservice.linkedin.model.LinkedInJobsDocument;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LinkedInFeignClientAdapter {

    private static final int DEFAULT_DELAY = 3;
    private final LinkedInFeignClient linkedInFeignClient;

    public LinkedInFeignClientAdapter(LinkedInFeignClient linkedInFeignClient) {
        this.linkedInFeignClient = linkedInFeignClient;
    }

    public LinkedInJobsDocument getJobs(LinkedInFilter filter) {
        try {
            String response = linkedInFeignClient.getJobs(
                    filter.getSearchTerm(),
                    filter.getLocation(),
                    filter.getRemote(),
                    filter.getJobType(),
                    filter.getEasyApply(),
                    filter.getLinkedinCompanyIds(),
                    filter.getHoursOld() != null ? String.valueOf(filter.getHoursOld() * 3600) : "r",
                    filter.getPage() == null ? 0 : null,
                    filter.getPage() != null ? (filter.getPage() * 25) : null
            );
            sleepWithRandomDelay();
            Document document = Jsoup.parse(response);
            return new LinkedInJobsDocument(document);
        } catch (FeignException ex) {
            throw new AccessDeniedException("LINKEDIN getJobs exception:" + ex.getMessage());
        }
    }

    public LinkedInJobDescriptionDocument getJob(String externalId) {
        try {
            sleepWithRandomDelay();
            String response = linkedInFeignClient.getJob(externalId);
            Document document = Jsoup.parse(response);

            return new LinkedInJobDescriptionDocument(document);
        } catch (FeignException ex) {
            throw new AccessDeniedException("LINKEDIN getJob[id=" + externalId + "] exception:" + ex.getMessage());
        }
    }

    private void sleepWithRandomDelay() {
        try {
            Thread.sleep((long) (DEFAULT_DELAY + Math.random() * 2) * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}