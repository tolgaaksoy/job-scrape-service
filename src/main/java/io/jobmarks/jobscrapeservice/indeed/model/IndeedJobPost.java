package io.jobmarks.jobscrapeservice.indeed.model;

import io.jobmarks.jobscrapeservice.common.model.Compensation;
import io.jobmarks.jobscrapeservice.common.model.JobType;
import io.jobmarks.jobscrapeservice.common.model.Location;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "indeedJobPosts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndeedJobPost {
    @Id
    private String id;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private String externalId;

    private String title;
    private String companyName;
    private String jobUrl;
    private Location location;

    private String description;
    private List<String> seniority;
    private List<String> industry;
    private List<String> jobFunction;

    private String applicantsCount;

    private String companyUrl;

    private List<JobType> jobType;
    private Compensation compensation;
    private Instant datePosted;
    private List<String> benefits;
    private List<String> emails;
    private Integer numUrgentWords;
    private Boolean isRemote;

    private List<String> labels;
}
