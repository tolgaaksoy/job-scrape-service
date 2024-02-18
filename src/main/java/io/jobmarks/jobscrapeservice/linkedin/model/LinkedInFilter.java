package io.jobmarks.jobscrapeservice.linkedin.model;


import io.jobmarks.jobscrapeservice.common.model.BaseFilter;
import io.jobmarks.jobscrapeservice.common.model.Country;
import io.jobmarks.jobscrapeservice.common.model.JobType;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinkedInFilter extends BaseFilter {

    private String searchTerm;
    private String location;
    private Country country;
    private Boolean remote;
    private JobType jobType;
    private Boolean easyApply;
    private List<Integer> linkedinCompanyIds;
    private Integer hoursOld;

    public String getLocation() {
        if (this.location == null || this.location.isEmpty()) {
            return "worldwide";
        }
        return location;
    }

    public String getRemote() {
        if (this.remote != null) {
            return "2";
        }
        return null;
    }

    public String getJobType() {
        if (this.jobType != null) {
            return jobType.getLinkedIn();
        }
        return null;
    }

    public String getLinkedinCompanyIds() {
        if (this.linkedinCompanyIds != null && !this.linkedinCompanyIds.isEmpty()) {
            return this.linkedinCompanyIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
        }
        return null;
    }
}

