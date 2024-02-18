package io.jobmarks.jobscrapeservice.linkedin.model;

import io.jobmarks.jobscrapeservice.common.model.Compensation;
import io.jobmarks.jobscrapeservice.common.model.Location;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class LinkedinJobCardElement {
    private static final String BASE_CARD_FULL_LINK_SELECTOR = "a.base-card__full-link";
    private static final String SALARY_INFO_SELECTOR = "span.job-search-card__salary-info";
    private static final String SR_ONLY_SELECTOR = "span.sr-only";
    private static final String BASE_SEARCH_CARD_SUBTITLE_SELECTOR = "h4.base-search-card__subtitle";
    private static final String BASE_SEARCH_CARD_METADATA_SELECTOR = "div.base-search-card__metadata";
    private static final String JOB_SEARCH_CARD_LOCATION_SELECTOR = "span.job-search-card__location";
    private static final String JOB_SEARCH_CARD_LIST_DATE_SELECTOR = "time.job-search-card__listdate";
    private static final String BENEFITS_SELECTOR = "span.result-benefits__text";

    private final Element jobCard;
    private final String externalId;

    public LinkedinJobCardElement(Element jobCard) {
        this.jobCard = jobCard;
        this.externalId = extractExternalId();
    }

    public String getExternalId() {
        return this.externalId;
    }

    public String getJobUrl() {
        return "https://www.linkedin.com/jobs/view/" + getExternalId();
    }

    public Compensation getCompensation() {
        Element salaryTag = jobCard.selectFirst(SALARY_INFO_SELECTOR);
        if (salaryTag != null) {
            String salaryText = salaryTag.text().trim();
            String[] salaryValues = salaryText.split("-");
            String salaryMin = String.valueOf(parseCurrency(salaryValues[0]));
            String salaryMax = String.valueOf(parseCurrency(salaryValues[1]));
            String currency = salaryText.charAt(0) != '$' ? "USD" : String.valueOf(salaryText.charAt(0));

            return Compensation.builder()
                    .minAmount(Float.parseFloat(salaryMin))
                    .maxAmount(Float.parseFloat(salaryMax))
                    .currency(currency)
                    .build();
        }
        return null;
    }

    private int parseCurrency(String value) {
        try {
            return (int) Math.round(Double.parseDouble(value.replaceAll("[^\\d.]", "")));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String getTitle() {
        Element titleTag = jobCard.selectFirst(SR_ONLY_SELECTOR);
        return (titleTag != null) ? titleTag.text().trim() : "N/A";
    }

    public String getCompanyName() {
        Element companyTag = jobCard.selectFirst(BASE_SEARCH_CARD_SUBTITLE_SELECTOR);
        Element companyATag = (companyTag != null) ? companyTag.selectFirst("a") : null;
        return (companyATag != null) ? companyATag.text().trim() : "N/A";
    }

    public String getCompanyUrl() {
        Element companyTag = jobCard.selectFirst(BASE_SEARCH_CARD_SUBTITLE_SELECTOR);
        Element companyATag = (companyTag != null) ? companyTag.selectFirst("a") : null;

        if (companyATag != null) {
            return processCompanyATag(companyATag);
        } else {
            return "";
        }
    }

    private String processCompanyATag(Element companyATag) {
        String href = companyATag.attr("href");
        try {
            URI uri = new URI(href);
            return new URI(uri.getScheme(), uri.getHost(), uri.getPath(), null).toString();
        } catch (URISyntaxException e) {
            // Handle URISyntaxException if needed
            e.printStackTrace(); // Print the stack trace for debugging purposes
        }
        return "";
    }

    public Location getLocation() {
        Element metadataCard = jobCard.selectFirst(BASE_SEARCH_CARD_METADATA_SELECTOR);
        Location location = new Location();
        if (metadataCard != null) {
            Element locationTag = metadataCard.selectFirst(JOB_SEARCH_CARD_LOCATION_SELECTOR);
            String locationString = (locationTag != null) ? locationTag.text().trim() : "N/A";

            String[] parts = locationString.split(", ");
            if (parts.length == 2) {
                location.setCity(parts[0]);
                location.setState(parts[1]);
            } else if (parts.length == 3) {
                location.setCity(parts[0]);
                location.setState(parts[1]);
                location.setCountry(parts[2]);
            }
        }
        return location;
    }

    public Date getDatePosted() {
        Element metadataCard = jobCard.selectFirst(BASE_SEARCH_CARD_METADATA_SELECTOR);

        Element datetimeTag = (metadataCard != null) ? metadataCard.selectFirst(JOB_SEARCH_CARD_LIST_DATE_SELECTOR) : null;
        if (datetimeTag != null && datetimeTag.hasAttr("datetime")) {
            String datetimeStr = datetimeTag.attr("datetime");
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(datetimeStr, formatter);
                return java.sql.Date.valueOf(date);
            } catch (Exception e) {
                e.printStackTrace(); // Print the stack trace for debugging purposes
            }
        }
        return null;
    }

    public String getBenefits() {
        Element benefitsTag = jobCard.selectFirst(BENEFITS_SELECTOR);
        return (benefitsTag != null) ? benefitsTag.text() : null;
    }

    private String extractExternalId() {
        Element hrefTag = jobCard.selectFirst(BASE_CARD_FULL_LINK_SELECTOR);
        if (hrefTag != null && hrefTag.hasAttr("href")) {
            String href = hrefTag.attr("href").split("\\?")[0];
            return href.split("-")[href.split("-").length - 1];
        } else {
            return "N/A";
        }
    }
}
