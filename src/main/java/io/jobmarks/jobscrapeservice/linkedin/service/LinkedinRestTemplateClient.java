package io.jobmarks.jobscrapeservice.linkedin.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class LinkedinRestTemplateClient {

    private static final String HEADER_AUTHORITY = "www.linkedin.com";
    private static final String HEADER_ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7";
    private static final String HEADER_ACCEPT_LANGUAGE = "en-US,en;q=0.9";
    private static final String HEADER_CACHE_CONTROL = "max-age=0";
    private static final String HEADER_SEC_CH_UA = "\"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"";
    private static final String HEADER_UPGRADE_INSECURE_REQUESTS = "1";
    private static final String HEADER_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

    @Value("${linkedin.search-jobs.api.url}")
    private String searchJobsApiUrl;
    @Value("${linkedin.get-job-details.api.url}")
    private String jobDetailsApiUrl;

    private final RestTemplate restTemplate;

    public LinkedinRestTemplateClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String searchJobs(String keywords, String location, String remote, String jobType, Boolean easyApply, String companyIds, String secondsOld, Integer page, Integer start) {
        HttpEntity<?> entity = new HttpEntity<>(createCommonHttpHeaders());
        String url = buildSearchJobsUrl(keywords, location, remote, jobType, easyApply, companyIds, secondsOld, page, start);

        return exchange(url, entity);
    }

    public String fetchJobDetails(String externalId) {
        HttpEntity<?> entity = new HttpEntity<>(createCommonHttpHeaders());
        String url = jobDetailsApiUrl + externalId;

        return exchange(url, entity);
    }

    private static HttpHeaders createCommonHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("authority", HEADER_AUTHORITY);
        headers.set("accept", HEADER_ACCEPT);
        headers.set("accept-language", HEADER_ACCEPT_LANGUAGE);
        headers.set("cache-control", HEADER_CACHE_CONTROL);
        headers.set("sec-ch-ua", HEADER_SEC_CH_UA);
        headers.set("upgrade-insecure-requests", HEADER_UPGRADE_INSECURE_REQUESTS);
        headers.set("user-agent", HEADER_USER_AGENT);
        return headers;
    }

    private String buildSearchJobsUrl(String keywords, String location, String remote, String jobType, Boolean easyApply, String companyIds, String secondsOld, Integer page, Integer start) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(searchJobsApiUrl)
                .queryParam("keywords", keywords)
                .queryParam("location", location)
                .queryParam("f_WT", remote)
                .queryParam("f_JT", jobType)
                .queryParam("f_AL", easyApply)
                .queryParam("f_C", companyIds)
                .queryParam("f_TPR", secondsOld)
                .queryParam("pageNum", page)
                .queryParam("start", start);
        return builder.toUriString();
    }

    private String exchange(String url, HttpEntity<?> entity) {
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
}