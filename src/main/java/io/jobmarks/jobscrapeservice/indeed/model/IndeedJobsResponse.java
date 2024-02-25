package io.jobmarks.jobscrapeservice.indeed.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndeedJobsResponse {
    private String jobTitle;
}
