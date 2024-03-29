package io.jobmarks.jobscrapeservice.linkedin.model;

import io.jobmarks.jobscrapeservice.common.model.Compensation;
import io.jobmarks.jobscrapeservice.common.model.Location;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "linkedInJobPosts")
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkedInJobPost {

    @Id
    private String id;

    private String externalId;

    private String title;
    private String companyName;
    private String jobUrl;
    private Location location;

    private String description;
    private List<String>  seniority;
    private List<String>  industry;
    private List<String>  jobFunction;

    private String applicantsCount;

    private String companyUrl;

    private List<String> jobType;
    private Compensation compensation;
    private Date datePosted;
    private String benefits;
    private List<String> emails;
    private Integer numUrgentWords;
    private Boolean isRemote;

}
