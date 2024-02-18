package io.jobmarks.jobscrapeservice.linkedin.controller;


import io.jobmarks.jobscrapeservice.linkedin.model.LinkedInFilter;
import io.jobmarks.jobscrapeservice.linkedin.service.LinkedInScrapeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/linkedin")
public class LinkedInScrapeController {

    private final LinkedInScrapeService linkedInScrapeService;

    public LinkedInScrapeController(LinkedInScrapeService linkedInScrapeService) {
        this.linkedInScrapeService = linkedInScrapeService;
    }

    @PostMapping("/start")
    public ResponseEntity startScrape(@RequestBody LinkedInFilter linkedınFilter) {
        return ResponseEntity.ok(linkedInScrapeService.scrape(linkedınFilter));
    }

}
