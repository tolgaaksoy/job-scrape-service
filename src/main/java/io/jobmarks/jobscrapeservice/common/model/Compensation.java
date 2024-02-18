package io.jobmarks.jobscrapeservice.common.model;

import lombok.*;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Compensation {
    private CompensationInterval interval;
    private Float minAmount;
    private Float maxAmount;
    private String currency;
}
