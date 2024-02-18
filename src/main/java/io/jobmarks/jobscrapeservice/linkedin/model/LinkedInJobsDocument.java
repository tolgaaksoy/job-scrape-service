package io.jobmarks.jobscrapeservice.linkedin.model;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LinkedInJobsDocument {

    private static final String JOB_CARD_SELECTOR = "div.base-search-card";

    private final Document document;
    private final List<LinkedinJobCardElement> linkedInJobCardElements;

    public LinkedInJobsDocument(Document document) {
        if (document == null) {
            throw new IllegalArgumentException("Document cannot be null");
        }
        this.document = document;
        this.linkedInJobCardElements = getLinkedInJobCardElementsFromDocument();
    }

    public List<LinkedinJobCardElement> getLinkedInJobCardElements() {
        return this.linkedInJobCardElements;
    }

    private List<LinkedinJobCardElement> getLinkedInJobCardElementsFromDocument() {
        Elements jobCards = document.select(JOB_CARD_SELECTOR);

        if (jobCards.isEmpty()) {
            return Collections.emptyList();
        }

        List<LinkedinJobCardElement> jobCardElements = new ArrayList<>(jobCards.size());

        for (Element element : jobCards) {
            jobCardElements.add(new LinkedinJobCardElement(element));
        }

        return Collections.unmodifiableList(jobCardElements);
    }
}
