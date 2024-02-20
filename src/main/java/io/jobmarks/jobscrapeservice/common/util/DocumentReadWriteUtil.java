package io.jobmarks.jobscrapeservice.common.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DocumentReadWriteUtil {

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
            //log.error("Write file exception:  folder = src/main/resources/{} ~ file name = {} ~ message: {}", folder, fileName, e.getMessage());
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
