package io.jobmarks.jobscrapeservice.common.config;

import feign.Client;
import feign.Request;
import feign.Response;
import io.jobmarks.jobscrapeservice.common.model.ApiLog;
import io.jobmarks.jobscrapeservice.common.repository.ApiLogRepository;

import java.io.IOException;
import java.time.Instant;

public class FeignClientWrapper implements Client {
    private final Client delegate;
    private final ApiLogRepository apiLogRepository;

    public FeignClientWrapper(Client delegate, ApiLogRepository apiLogRepository) {
        this.delegate = delegate;
        this.apiLogRepository = apiLogRepository;
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        ApiLog apiLog = ApiLogContextHolder.getApiLog();
        Response response = delegate.execute(request, options);

        if (apiLog != null) {
            apiLog.setStatus(response.status());
            apiLog.setResponseTimestamp(Instant.now());
            apiLog.setDuration(apiLog.getResponseTimestamp().toEpochMilli() - apiLog.getRequestTimestamp().toEpochMilli());
            apiLogRepository.save(apiLog);
        }
        ApiLogContextHolder.clear();
        return response;
    }

}
