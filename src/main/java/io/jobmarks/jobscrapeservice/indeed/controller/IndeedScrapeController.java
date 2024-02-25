package io.jobmarks.jobscrapeservice.indeed.controller;

import io.jobmarks.jobscrapeservice.indeed.model.IndeedFilter;
import io.jobmarks.jobscrapeservice.indeed.service.IndeedScrapeService;
import io.jobmarks.jobscrapeservice.linkedin.model.JobResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/indeed")
public class IndeedScrapeController {
    private final IndeedScrapeService indeedScrapeService;

    public IndeedScrapeController(IndeedScrapeService indeedScrapeService) {
        this.indeedScrapeService = indeedScrapeService;
    }

    @PostMapping("/start")
    public ResponseEntity<JobResponse> startScrape(@RequestBody IndeedFilter indeedFilter) {
        return ResponseEntity.ok(indeedScrapeService.scrape(indeedFilter));
    }

}
