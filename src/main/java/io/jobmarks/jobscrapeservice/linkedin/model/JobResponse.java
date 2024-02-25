package io.jobmarks.jobscrapeservice.linkedin.model;

import io.jobmarks.jobscrapeservice.indeed.model.IndeedJobPost;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {
    private List<LinkedInJobPost> linkedInJobPosts;
    private List<IndeedJobPost> indeedJobPosts;
}
