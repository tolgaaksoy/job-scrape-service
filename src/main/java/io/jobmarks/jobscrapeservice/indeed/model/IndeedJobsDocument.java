package io.jobmarks.jobscrapeservice.indeed.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class IndeedJobsDocument {

    private static final String MOSAIC_DATA_PATTERN = "window\\.mosaic\\.initialData = (\\{.*?}});";
    private final Document document;
    private final JSONObject jobs;
    private List<IndeedJobCardElement> indeedJobCardElements;

    public IndeedJobsDocument(Document document) {
        if (document == null) {
            throw new IllegalArgumentException("Document cannot be null");
        }
        this.document = document;
        this.jobs = parseJobs();
    }

    private JSONObject parseJobs() {
        Element scriptTag = findMosaicScript();
        if (scriptTag == null) {
            return null;
        }

        String scriptStr = scriptTag.html();
        Matcher m = Pattern.compile(MOSAIC_DATA_PATTERN, Pattern.DOTALL).matcher(scriptStr);

        if (m.find()) {
            try {
                return new JSONObject(m.group(1));
            } catch (JSONException e) {
                // Log the exception and consider re-throwing as a custom checked exception
                // or returning an Optional.empty() if you prefer not to use exceptions
                throw new RuntimeException("Failed to parse job data", e);
            }
        }
        return null;
    }

    private Element findMosaicScript() {
        Elements scriptTags = document.getElementsByTag("script");
        for (Element tag : scriptTags) {
            if (tag.html().contains("window.mosaic.initialData")) {
                return tag;
            }
        }
        return null;
    }

    public List<IndeedJobCardElement> getIndeedJobCardElements() {
        if (this.indeedJobCardElements != null) {
            return Collections.unmodifiableList(indeedJobCardElements);
        }

        List<IndeedJobCardElement> results = new ArrayList<>();

        JSONObject metaData = jobs.optJSONObject("publicMetadata");
        if (metaData == null) {
            return Collections.emptyList();
        }

        JSONObject provider = metaData.optJSONObject("mosaicProviderJobCardsModel");
        if (provider == null) {
            return Collections.emptyList();
        }

        JSONArray jobsArray = provider.optJSONArray("results");
        if (jobsArray == null) {
            return Collections.emptyList();
        }

        ObjectMapper objectMapper = new ObjectMapper();

        for (int i = 0; i < jobsArray.length(); i++) {
            try {
                JSONObject job = jobsArray.getJSONObject(i);
                IndeedJobCardElement result = objectMapper.readValue(job.toString(), IndeedJobCardElement.class);
                results.add(result);
            } catch (JSONException | JsonProcessingException e) {
                // Consider logging the exception and continue processing the remaining jobs
            }
        }

        this.indeedJobCardElements = results;
        return Collections.unmodifiableList(results);
    }
}