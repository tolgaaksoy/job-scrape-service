package io.jobmarks.jobscrapeservice.linkedin.model;

import io.micrometer.common.util.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Collections;
import java.util.List;

public class LinkedInJobDescriptionDocument {

    private static final String CONTENT_SELECTOR = "div.show-more-less-html__markup";
    private static final String EMPLOYMENT_TYPE_SELECTOR = "h3.description__job-criteria-subheader:contains(Employment type)";

    private final Document document;

    public LinkedInJobDescriptionDocument(Document document) {
        if (document == null) {
            throw new IllegalArgumentException("Document cannot be null");
        }
        this.document = document;
    }

    public String getDescription() {
        Element divContent = document.selectFirst(CONTENT_SELECTOR);
        return (divContent != null) ? divContent.html() : null;
    }

    public List<String> getJobType() {
        Element h3Tag = document.selectFirst(EMPLOYMENT_TYPE_SELECTOR);
        String employmentType = (h3Tag != null && h3Tag.nextElementSibling() != null) ? h3Tag.nextElementSibling().text() : null;

        if (StringUtils.isNotBlank(employmentType)) {
            // Your logic to map employmentType to JobType enum
            return Collections.singletonList(employmentType.toUpperCase());
        } else {
            return Collections.emptyList();
        }
    }
}
