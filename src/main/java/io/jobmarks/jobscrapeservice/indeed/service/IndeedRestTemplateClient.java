package io.jobmarks.jobscrapeservice.indeed.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndeedRestTemplateClient {

    @Value("${indeed.search-jobs.api.url}")
    private String indeedUrl;
    @Value("${indeed.search-jobs.api.user-agent}")
    private String userAgent;
    @Value("${indeed.get-job-details.api.url}")
    private String apiUrl;
    @Value("${indeed.get-job-details.api.key}")
    private String indeedApiKey;
    private final RestTemplate restTemplate;

    public IndeedRestTemplateClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String searchJobs(String keywords, String location, String distance, String jobType, String easyApply, Integer hoursOld, String filter, Integer start, String sort) {
        URI searchUri = buildSearchUri(keywords, location, distance, jobType, easyApply, hoursOld, filter, start, sort);
        HttpEntity<?> entity = new HttpEntity<>(buildCommonHttpHeaders());
        ResponseEntity<String> response = restTemplate.exchange(searchUri, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    public String fetchJobDetails(List<String> jobKeys) {
        String payload = buildJobDetailsPayload(jobKeys);
        HttpHeaders headers = buildApiHttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }

    private URI buildSearchUri(String keywords, String location, String distance, String jobType, String easyApply, Integer hoursOld, String filter, Integer start, String sort) {
        return UriComponentsBuilder.fromHttpUrl(indeedUrl)
                .queryParam("q", keywords)
                .queryParam("l", location)
                .queryParam("radius", distance)
                .queryParam("sc", jobType)
                .queryParam("iafilter", easyApply)
                .queryParam("fromage", hoursOld)
                .queryParam("filter", filter)
                .queryParam("start", start)
                .queryParam("sort", sort)
                .build()
                .toUri();
    }

    private HttpHeaders buildCommonHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Host", "www.indeed.com");
        headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.set("Accept-Language", "en-US,en;q=0.9");
        headers.set("User-Agent", userAgent);
        return headers;
    }

    private HttpHeaders buildApiHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("indeed-api-key", indeedApiKey);
        headers.set("Host", "apis.indeed.com");
        headers.set("indeed-locale", "en-US");
        headers.set("accept-language", "en-US,en;q=0.9");
        headers.set("user-agent", userAgent);
        headers.set("indeed-app-info", "appv=193.1; appid=com.indeed.jobsearch; osv=16.6.1; os=ios; dtype=phone");
        headers.set("indeed-co", "US");
        return headers;
    }

    private String buildJobDetailsPayload(List<String> jobKeys) {
        String jobKeysJson = jobKeys.stream()
                .map(key -> "\\\"" + key + "\\\"")
                .collect(Collectors.joining(", ", "[", "]"));
        return String.format("{\"query\": \"query GetJobData { jobData(input: { jobKeys: %s }) { results { job { key title description { html } location { countryName countryCode city postalCode streetAddress formatted { short long } } compensation { baseSalary { unitOfWork range { ... on Range { min max } }} currencyCode } attributes { label } employer { relativeCompanyPageUrl } recruit { viewJobUrl detailedSalary workSchedule } } } } }\"}", jobKeysJson);
    }
}