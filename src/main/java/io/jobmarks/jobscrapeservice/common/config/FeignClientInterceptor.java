package io.jobmarks.jobscrapeservice.common.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.jobmarks.jobscrapeservice.common.model.ApiLog;
import io.jobmarks.jobscrapeservice.common.repository.ApiLogRepository;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class FeignClientInterceptor implements RequestInterceptor {

    private final ApiLogRepository apiLogRepository;

    public FeignClientInterceptor(ApiLogRepository apiLogRepository) {
        this.apiLogRepository = apiLogRepository;
    }

    @Override
    public void apply(RequestTemplate template) {
        ApiLog apiLog = new ApiLog();
        apiLog.setRequestTimestamp(Instant.now());
        apiLog.setMethod(template.method());
        apiLog.setUrl(template.request().url());
        apiLog.setHeaders(template.headers().toString());
        if (template.body() != null) {
            byte[] bodyData = template.body();
            String bodyContent = new String(bodyData, StandardCharsets.UTF_8);
            apiLog.setBody(bodyContent);
        }
        apiLog = apiLogRepository.save(apiLog);

        ApiLogContextHolder.setApiLog(apiLog);
    }
}