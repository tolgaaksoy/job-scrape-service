package io.jobmarks.jobscrapeservice.common.model;

import lombok.Getter;

@Getter
public enum JobType {
    FULL_TIME("F"),
    PART_TIME("P"),
    INTERNSHIP("I"),
    CONTRACT("C"),
    TEMPORARY("T");
    private final String linkedIn;

    JobType(String linkedIn) {
        this.linkedIn = linkedIn;
    }

}
