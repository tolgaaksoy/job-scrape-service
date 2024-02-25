package io.jobmarks.jobscrapeservice.common.model;

import lombok.Getter;

@Getter
public enum JobType {
    FULL_TIME("F", "Full-time"),
    PART_TIME("P", "Part-time"),
    INTERNSHIP("I", "Internship"),
    CONTRACT("C", "Contract"),
    TEMPORARY("T", "Temporary"),
    UNDEFINED("U", "Undefined");

    private final String linkedIn;
    private final String indeed;

    JobType(String linkedIn, String indeed) {
        this.linkedIn = linkedIn;
        this.indeed = indeed;
    }

    public static JobType fromIndeed(String indeed) {
        for (JobType jobType : values()) {
            if (jobType.indeed.equals(indeed)) {
                return jobType;
            }
        }
        return UNDEFINED;
    }
}
