package io.jobmarks.jobscrapeservice.common.model;

import lombok.Getter;

@Getter
public enum Site {
    LINKEDIN("linkedin"),
    INDEED("indeed"),
    ZIP_RECRUITER("zip_recruiter"),
    GLASSDOOR("glassdoor");

    private final String value;

    Site(String value) {
        this.value = value;
    }

}
