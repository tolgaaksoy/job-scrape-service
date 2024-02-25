package io.jobmarks.jobscrapeservice.common.model;

import lombok.*;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    private String country;
    private String city;
    private String state;

}
