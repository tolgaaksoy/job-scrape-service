package io.jobmarks.jobscrapeservice.common.model;

import lombok.Getter;

@Getter
public enum CompensationInterval {
    YEARLY("yearly"),
    MONTHLY("monthly"),
    WEEKLY("weekly"),
    DAILY("daily"),
    HOURLY("hourly");

    private final String value;

    CompensationInterval(String value) {
        this.value = value;
    }

    public static String getInterval(String payPeriod) {
        return switch (payPeriod) {
            case "YEAR" -> YEARLY.getValue();
            case "HOUR" -> HOURLY.getValue();
            default -> CompensationInterval.valueOf(payPeriod).getValue();
        };
    }

    public String getValue() {
        return value;
    }
}
