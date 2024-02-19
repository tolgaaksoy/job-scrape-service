package io.jobmarks.jobscrapeservice.linkedin.model;

import io.micrometer.common.util.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Collections;
import java.util.List;

public class LinkedInJobDescriptionDocument {
    private static final String CONTENT_SELECTOR = "div.show-more-less-html__markup";
    private static final String NUMBER_OF_APPLICANTS_SELECTOR = "span.num-applicants__caption";
    private static final String EMPLOYMENT_TYPE_SELECTOR = "h3.description__job-criteria-subheader:contains(Employment type)";
    private static final String SENIORITY_LEVEL_SELECTOR = "h3.description__job-criteria-subheader:contains(Seniority level)";
    private static final String JOB_FUNCTION_SELECTOR = "h3.description__job-criteria-subheader:contains(Job function)";
    private static final String INDUSTRY_SELECTOR = "h3.description__job-criteria-subheader:contains(Industries)";
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

    public String getApplicantsCount() {
        Element spanTag = document.selectFirst(NUMBER_OF_APPLICANTS_SELECTOR);
        return (spanTag != null) ? spanTag.text() : null;
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

    public List<String> getSeniorityLevel() {
        Element h3Tag = document.selectFirst(SENIORITY_LEVEL_SELECTOR);
        String seniorityLevel = (h3Tag != null && h3Tag.nextElementSibling() != null) ? h3Tag.nextElementSibling().text() : null;
        if (StringUtils.isNotBlank(seniorityLevel)) {
            return Collections.singletonList(seniorityLevel.toUpperCase());
        } else {
            return Collections.emptyList();
        }
    }

    public List<String> getJobFunction() {
        Element h3Tag = document.selectFirst(JOB_FUNCTION_SELECTOR);
        String jobFunction = (h3Tag != null && h3Tag.nextElementSibling() != null) ? h3Tag.nextElementSibling().text() : null;
        if (StringUtils.isNotBlank(jobFunction)) {
            return Collections.singletonList(jobFunction.toUpperCase());
        } else {
            return Collections.emptyList();
        }
    }

    public List<String> getIndustries() {
        Element h3Tag = document.selectFirst(INDUSTRY_SELECTOR);
        String industry = (h3Tag != null && h3Tag.nextElementSibling() != null) ? h3Tag.nextElementSibling().text() : null;
        if (StringUtils.isNotBlank(industry)) {
            return Collections.singletonList(industry.toUpperCase());
        } else {
            return Collections.emptyList();
        }
    }
}
