package io.jobmarks.jobscrapeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableFeignClients
@EnableMongoAuditing
public class JobScrapeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobScrapeServiceApplication.class, args);
    }

}
