package io.jobmarks.jobscrapeservice.indeed.model;

import io.jobmarks.jobscrapeservice.common.model.BaseFilter;
import io.jobmarks.jobscrapeservice.common.model.Country;
import io.jobmarks.jobscrapeservice.common.model.JobType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndeedFilter extends BaseFilter {

    private String searchTerm;
    private String location;
    private String distance;
    private Country country;
    private Boolean remote;
    private JobType jobType;
    private Boolean easyApply;
    private Integer hoursOld;
}
