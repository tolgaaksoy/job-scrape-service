package io.jobmarks.jobscrapeservice.common.config;

import feign.Client;
import io.jobmarks.jobscrapeservice.common.repository.ApiLogRepository;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(FeignClientsConfiguration.class)
public class FeignConfig {

    private final ApiLogRepository apiLogRepository;

    public FeignConfig(ApiLogRepository apiLogRepository) {
        this.apiLogRepository = apiLogRepository;
    }

    @Bean
    public FeignClientInterceptor feignClientInterceptor() {
        return new FeignClientInterceptor(apiLogRepository);
    }

    @Bean
    public Client feignClient() {
        return new FeignClientWrapper(new Client.Default(null, null), apiLogRepository);
    }

}
