package io.jobmarks.jobscrapeservice.indeed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jobmarks.jobscrapeservice.common.exception.AccessDeniedException;
import io.jobmarks.jobscrapeservice.indeed.model.IndeedFilter;
import io.jobmarks.jobscrapeservice.indeed.model.IndeedJobDescriptionDocument;
import io.jobmarks.jobscrapeservice.indeed.model.IndeedJobsDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class IndeedClientAdapter {
    private static final int DEFAULT_DELAY_MS = 3000;
    private static final int RANDOM_DELAY_MS = 2000;

    private final IndeedRestTemplateClient indeedRestTemplateClient;
    private final ObjectMapper objectMapper;

    private final Random random = new Random();

    public IndeedClientAdapter(IndeedRestTemplateClient indeedRestTemplateClient, ObjectMapper objectMapper) {
        this.indeedRestTemplateClient = indeedRestTemplateClient;
        this.objectMapper = objectMapper;
    }

    public IndeedJobsDocument getJobs(IndeedFilter filter) {
        String response = fetchJobSearchResponse(filter);
        applyRandomDelay();
        return parseJobSearchResponse(response);
    }

    public List<IndeedJobDescriptionDocument> getJobDetails(List<String> jobKeys) {
        String response = indeedRestTemplateClient.fetchJobDetails(jobKeys);
        return parseJobDetailsResponse(response);
    }

    private String fetchJobSearchResponse(IndeedFilter filter) {
        try {
            String searchCriteria = buildSearchCriteria(filter);
            Integer fromAge = calculateFromAge(filter.getHoursOld());
            Integer pageOffset = calculatePageOffset(filter.getPage(), filter.getOffset());
            String easyApply = Boolean.TRUE.equals(filter.getEasyApply()) ? "1" : null;
            return indeedRestTemplateClient.searchJobs(
                    filter.getSearchTerm(),
                    filter.getLocation(),
                    filter.getDistance(),
                    searchCriteria,
                    easyApply,
                    fromAge,
                    "0",
                    pageOffset,
                    "date"
            );
        } catch (Exception ex) {
            throw new AccessDeniedException("Failed to retrieve jobs from Indeed: " + ex.getMessage(), ex);
        }
    }

    private IndeedJobsDocument parseJobSearchResponse(String response) {
        Document document = Jsoup.parse(response);
        return new IndeedJobsDocument(document);
    }

    private List<IndeedJobDescriptionDocument> parseJobDetailsResponse(String response) {
        try {
            JsonNode results = objectMapper.readTree(response)
                    .path("data")
                    .path("jobData")
                    .path("results");
            List<IndeedJobDescriptionDocument> jobs = new ArrayList<>();
            for (JsonNode result : results) {
                jobs.add(objectMapper.treeToValue(result, IndeedJobDescriptionDocument.class));
            }
            return jobs;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse job details response", e);
        }
    }

    private String buildSearchCriteria(IndeedFilter filter) {
        List<String> criteria = new ArrayList<>();
        if (Boolean.TRUE.equals(filter.getRemote())) {
            criteria.add("attr(DSQF7)");
        }
        if (filter.getJobType() != null) {
            criteria.add(String.format("jt(%s)", filter.getJobType().getIndeed()));
        }
        return criteria.isEmpty() ? null : "0kf:" + String.join("", criteria) + ";";
    }

    private Integer calculateFromAge(int hoursOld) {
        return hoursOld > 0 ? Math.max(hoursOld / 24, 1) : null;
    }

    private Integer calculatePageOffset(Integer page, Integer offset) {
        return page != null ? (offset + page * 10) : null;
    }

    private void applyRandomDelay() {
        try {
            Thread.sleep((long) DEFAULT_DELAY_MS + random.nextInt(RANDOM_DELAY_MS));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}