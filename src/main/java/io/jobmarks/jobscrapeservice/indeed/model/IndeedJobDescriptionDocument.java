package io.jobmarks.jobscrapeservice.indeed.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.jobmarks.jobscrapeservice.common.model.Compensation;
import io.jobmarks.jobscrapeservice.common.model.Location;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IndeedJobDescriptionDocument {

    @JsonProperty("job")
    private Job job;

    public String getExternalId() {
        return job.getKey();
    }

    public String getDescription() {
        return job.getDescription().getHtml();
    }

    public List<String> getIndustry() {
        return Collections.emptyList();
    }

    public List<String> getExperienceLevel() {
        return Collections.emptyList();
    }

    public List<String> getJobFunction() {
        return Collections.emptyList();
    }

    public Object getSkills() {
        return null;
    }

    public Object getRemote() {
        return null;
    }

    public String getCompanyUrl() {
        if (job == null || job.getEmployer() == null) {
            return null;
        }
        return job.getEmployer().getRelativeCompanyPageUrl();
    }

    public Compensation getCompensation() {
        if (job == null || job.getCompensation() == null) {
            return null;
        }

        Job.Compensation compensation = job.getCompensation();
        Job.Compensation.BaseSalary baseSalary = compensation.getBaseSalary();

        Float minAmount = Optional.ofNullable(baseSalary)
                .map(Job.Compensation.BaseSalary::getRange)
                .map(Job.Compensation.BaseSalary.Range::getMin)
                .map(Float::valueOf)
                .orElse(null);

        Float maxAmount = Optional.ofNullable(baseSalary)
                .map(Job.Compensation.BaseSalary::getRange)
                .map(Job.Compensation.BaseSalary.Range::getMax)
                .map(Float::valueOf)
                .orElse(null);

        return Compensation.builder()
                .currency(compensation.getCurrencyCode())
                .minAmount(minAmount)
                .maxAmount(maxAmount)
                .build();
    }

    public Location getLocation() {
        return Location.builder()
                .country(job.getLocation().getCountryName())
                .city(job.getLocation().getCity())
                .build();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Job {
        @JsonProperty("key")
        private String key;

        @JsonProperty("title")
        private String title;

        @JsonProperty("description")
        private Description description;

        @JsonProperty("location")
        private Location location;

        @JsonProperty("compensation")
        private Compensation compensation;

        @JsonProperty("employer")
        private Employer employer;

        @JsonProperty("attributes")
        private List<Attribute> attributes;

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Setter
        public static class Description {
            @JsonProperty("html")
            private String html;
        }

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Setter
        public static class Location {
            @JsonProperty("countryName")
            private String countryName;

            @JsonProperty("countryCode")
            private String countryCode;

            @JsonProperty("city")
            private String city;

            @JsonProperty("postalCode")
            private String postalCode;

            @JsonProperty("streetAddress")
            private String streetAddress;

            @JsonProperty("formatted")
            private Formatted formatted;

            @NoArgsConstructor
            @AllArgsConstructor
            @Getter
            @Setter
            public static class Formatted {
                @JsonProperty("short")
                private String shortFormat;

                @JsonProperty("long")
                private String longFormat;
            }
        }

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Setter
        public static class Compensation {
            @JsonProperty("baseSalary")
            private BaseSalary baseSalary;

            @JsonProperty("currencyCode")
            private String currencyCode;

            @NoArgsConstructor
            @AllArgsConstructor
            @Getter
            @Setter
            public static class BaseSalary {
                @JsonProperty("unitOfWork")
                private String unitOfWork;

                @JsonProperty("range")
                private Range range;

                @NoArgsConstructor
                @AllArgsConstructor
                @Getter
                @Setter
                public static class Range {
                    @JsonProperty("min")
                    private Integer min;

                    @JsonProperty("max")
                    private Integer max;
                }
            }
        }

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Setter
        public static class Employer {
            @JsonProperty("relativeCompanyPageUrl")
            private String relativeCompanyPageUrl;
        }

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Setter
        public static class Attribute {
            @JsonProperty("label")
            private String label;
        }
    }

}
