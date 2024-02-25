package io.jobmarks.jobscrapeservice.indeed.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.jobmarks.jobscrapeservice.common.model.JobType;
import lombok.*;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IndeedJobCardElement {

    @JsonProperty("company")
    private String companyName;
    @JsonProperty("jobkey")
    private String externalId;
    @JsonProperty("link")
    private String jobUrl;
    private long createDate;

    @JsonProperty("companyBrandingAttributes.headerImageUrl")
    private String headerImageUrl;

    @JsonProperty("companyBrandingAttributes.logoUrl")
    private String logoUrl;

    private double companyRating;
    private int companyReviewCount;

    private boolean d2iEnabled;
    private String displayTitle;
    private boolean dradisJob;
    private boolean employerAssistEnabled;
    private boolean employerResponsive;
    private Object enhancedAttributesModel;
    private List<Object> enticers;
    private boolean expired;
    private String extractTrackingUrls;
    private int fccompanyId;
    private Object featuredCompanyAttributes;
    private boolean featuredEmployer;
    private boolean featuredEmployerCandidate;
    private int feedId;
    private String formattedLocation;
    private String formattedRelativeTime;
    private boolean hideMetaData;
    private boolean hiringEventJob;
    private boolean indeedApplyEnabled;
    private boolean indeedApplyable;
    private boolean isNoResumeJob;
    private boolean isSubsidiaryJob;

    @JsonProperty("jobCardRequirementsModel.additionalRequirementsCount")
    private int additionalRequirementsCount;

    @JsonProperty("jobCardRequirementsModel.jobOnlyRequirements")
    private List<Object> jobOnlyRequirements;

    @JsonProperty("jobCardRequirementsModel.jobTagRequirements")
    private List<Object> jobTagRequirements;

    @JsonProperty("jobCardRequirementsModel.requirementsHeaderShown")
    private boolean requirementsHeaderShown;

    @JsonProperty("jobCardRequirementsModel.screenerQuestionRequirements")
    private List<Object> screenerQuestionRequirements;

    private String jobLocationCity;
    private String jobLocationState;
    private List<Object> jobTypes;


    private int locationCount;
    private String mobtk;
    private boolean newJob;
    private String normTitle;
    private boolean openInterviewsInterviewsOnTheSpot;
    private boolean openInterviewsJob;
    private boolean openInterviewsOffersOnTheSpot;
    private boolean openInterviewsPhoneJob;
    private int organicApplyStartCount;
    private long pubDate;

    @JsonProperty("rankingScoresModel.bid")
    private double rankingScoresBid;

    @JsonProperty("rankingScoresModel.eApply")
    private double rankingScoresEApply;

    @JsonProperty("rankingScoresModel.eQualified")
    private double rankingScoresEQualified;

    private boolean redirectToThirdPartySite;
    private boolean remoteLocation;
    private boolean resumeMatch;

    @JsonProperty("salarySnippet.currency")
    private String salarySnippetCurrency;

    @JsonProperty("salarySnippet.salaryTextFormatted")
    private boolean salarySnippetSalaryTextFormatted;

    private String screenerQuestionsURL;
    private String searchUID;
    private boolean showAttainabilityBadge;
    private boolean showCommutePromo;
    private boolean showEarlyApply;
    private boolean showJobType;
    private boolean showRelativeDate;
    private boolean showStrongerAppliedLabel;
    private boolean smartFillEnabled;
    private boolean smbD2iEnabled;
    private String snippet;
    private int sourceId;
    private boolean sponsored;
    private List<Object> taxoAttributes;
    private int taxoAttributesDisplayLimit;
    private List<Object> taxoLogAttributes;
    private List<TaxonomyAttribute> taxonomyAttributes;
    private String thirdPartyApplyUrl;

    @JsonProperty("tier.matchedPreferences.longMatchedPreferences")
    private List<Object> tierMatchedPreferencesLong;

    @JsonProperty("tier.matchedPreferences.stringMatchedPreferences")
    private List<Object> tierMatchedPreferencesString;

    private String title;
    private String truncatedCompany;
    private boolean urgentlyHiring;
    private String viewJobLink;
    private boolean vjFeaturedEmployerCandidate;

    public Instant getDatePosted() {
        return Instant.ofEpochMilli(pubDate);
    }

    public List<String> getBenefits() {
        if (taxoAttributes == null || taxoAttributes.isEmpty()) {
            return Collections.emptyList();
        }
        return taxonomyAttributes.stream()
                .filter(attribute -> "benefits".equals(attribute.getLabel()) && attribute.getAttributes() != null && !attribute.getAttributes().isEmpty())
                .flatMap(attribute -> attribute.getAttributes().stream())
                .map(TaxonomyAttribute.Attribute::getLabel)
                .toList();
    }

    public List<JobType> getJobType() {
        if (taxoAttributes == null || taxoAttributes.isEmpty()) {
            return List.of(JobType.UNDEFINED);
        }
        return taxonomyAttributes.stream()
                .filter(attribute -> "job-types".equals(attribute.getLabel()) && attribute.getAttributes() != null && !attribute.getAttributes().isEmpty())
                .flatMap(attribute -> attribute.getAttributes().stream())
                .map(TaxonomyAttribute.Attribute::getLabel)
                .map(JobType::fromIndeed)
                .toList();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaxonomyAttribute {
        private List<Attribute> attributes;
        private String label;

        @JsonIgnoreProperties(ignoreUnknown = true)
        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Attribute {
            private String label;
            private String suid;
        }
    }
}