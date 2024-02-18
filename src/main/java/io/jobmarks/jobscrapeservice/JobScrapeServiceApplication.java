package io.jobmarks.jobscrapeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class JobScrapeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobScrapeServiceApplication.class, args);
    }

}
