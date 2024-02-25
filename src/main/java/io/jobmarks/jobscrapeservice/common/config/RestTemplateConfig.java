package io.jobmarks.jobscrapeservice.common.config;

import io.jobmarks.jobscrapeservice.common.repository.ApiLogRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Configuration
public class RestTemplateConfig {

    private final ApiLogRepository apiLogRepository;

    public RestTemplateConfig(ApiLogRepository apiLogRepository) {
        this.apiLogRepository = apiLogRepository;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(List.of(new LoggingInterceptor(apiLogRepository)));
        return restTemplate;
    }
}
