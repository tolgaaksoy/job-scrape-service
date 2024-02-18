package io.jobmarks.jobscrapeservice.linkedin.service;

import feign.FeignException;
import io.jobmarks.jobscrapeservice.common.exception.AccessDeniedException;
import io.jobmarks.jobscrapeservice.linkedin.model.LinkedInFilter;
import io.jobmarks.jobscrapeservice.linkedin.model.LinkedInJobDescriptionDocument;
import io.jobmarks.jobscrapeservice.linkedin.model.LinkedInJobsDocument;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
@Component
public class LinkedInFeignClientAdapter {

    private static final int DEFAULT_DELAY = 3;
    private final LinkedInFeignClient linkedInFeignClient;

    public LinkedInFeignClientAdapter(LinkedInFeignClient linkedInFeignClient) {
        this.linkedInFeignClient = linkedInFeignClient;
    }

    public LinkedInJobsDocument getJobs(LinkedInFilter filter) {

        //For testing purposes
        Document inMemory = readResponseFromFile(filter.hashCode() + "p" + filter.getPage() + ".html", "getJobs");
        if (inMemory != null) {
            return new LinkedInJobsDocument(inMemory);
        }

        try {
            log.info("Filter: {}", filter);
            String response = linkedInFeignClient.getJobs(
                    filter.getSearchTerm(),
                    filter.getLocation(),
                    filter.getRemote(),
                    filter.getJobType(),
                    filter.getEasyApply(),
                    filter.getLinkedinCompanyIds(),
                    filter.getHoursOld() != null ? String.valueOf(filter.getHoursOld() * 3600) : "r",
                    filter.getPage() == null ? 0 : null,
                    filter.getPage() != null ? (filter.getPage() * 25) : null
            );

            sleepWithRandomDelay();
            Document document = Jsoup.parse(response);

            //For testing purposes
            writeResponseToFile(document, "getJobs",
                    filter.hashCode() + "p" + (filter.getPage() == null ? 0 : filter.getPage()) + ".html");

            return new LinkedInJobsDocument(document);
        } catch (FeignException ex) {
            throw new AccessDeniedException("LINKEDIN getJobs exception:" + ex.getMessage());
        }
    }

    public LinkedInJobDescriptionDocument getJob(String externalId) {
        //For testing purposes
        Document inMemory = readResponseFromFile(externalId + ".html", "getJob");
        if (inMemory != null) {
            return new LinkedInJobDescriptionDocument(inMemory);
        }

        try {
            sleepWithRandomDelay();
            String response = linkedInFeignClient.getJob(externalId);
            Document document = Jsoup.parse(response);

            //For testing purposes
            writeResponseToFile(document, "getJob", externalId + ".html");

            return new LinkedInJobDescriptionDocument(document);
        } catch (FeignException ex) {
            throw new AccessDeniedException("LINKEDIN getJob[id=" + externalId + "] exception:" + ex.getMessage());
        }
    }

    private void sleepWithRandomDelay() {
        try {
            Thread.sleep((long) (DEFAULT_DELAY + Math.random() * 2) * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    //For testing purposes
    private void writeResponseToFile(Document document, String folder, String fileName) {
        try {
            String resourceFolderPath = "src/main/resources/" + folder + "/";
            createFolderIfNotExists(resourceFolderPath);  // Ensure the folder exists

            File file = new File(resourceFolderPath + fileName);

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(document.outerHtml());
            }
        } catch (IOException e) {
            log.error("Write file exception:  folder = src/main/resources/{} ~ file name = {} ~ message: {}", folder, fileName, e.getMessage());
        }
    }

    //For testing purposes
    private void createFolderIfNotExists(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    //For testing purposes
    public Document readResponseFromFile(String fileName, String folder) {
        try {
            String resourceFolderPath = "src/main/resources/" + folder;
            File file = new File(resourceFolderPath + fileName);

            // Read the content of the file using Jsoup
            return Jsoup.parse(file, "UTF-8");
        } catch (IOException e) {
            //log.error("Read file exception:  folder = src/main/resources/{} ~ file name = {} ~ message: {}", folder, fileName, e.getMessage());
            return null;
        }
    }
}