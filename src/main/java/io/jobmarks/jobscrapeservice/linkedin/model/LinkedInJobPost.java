package io.jobmarks.jobscrapeservice.linkedin.model;

import io.jobmarks.jobscrapeservice.common.model.Compensation;
import io.jobmarks.jobscrapeservice.common.model.Location;
import lombok.*;

import java.util.Date;
import java.util.List;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkedInJobPost {
    private String title;
    private String companyName;
    private String jobUrl;
    private Location location;

    private String description;
    private String companyUrl;

    private List<String> jobType;
    private Compensation compensation;
    private Date datePosted;
    private String benefits;
    private List<String> emails;
    private Integer numUrgentWords;
    private Boolean isRemote;

}
