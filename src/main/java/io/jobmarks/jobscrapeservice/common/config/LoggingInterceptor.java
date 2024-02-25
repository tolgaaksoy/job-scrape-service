package io.jobmarks.jobscrapeservice.common.config;

import io.jobmarks.jobscrapeservice.common.model.ApiLog;
import io.jobmarks.jobscrapeservice.common.repository.ApiLogRepository;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;

public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    private final ApiLogRepository apiLogRepository;

    public LoggingInterceptor(ApiLogRepository apiLogRepository) {
        this.apiLogRepository = apiLogRepository;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ApiLog apiLog = new ApiLog();

        apiLog.setUrl(request.getURI().toString());
        apiLog.setMethod(request.getMethod().toString());
        apiLog.setHeaders(request.getHeaders().toString());
        apiLog.setRequestDate(Instant.now());

        long startTime = System.nanoTime();

        try {
            ClientHttpResponse response = execution.execute(request, body);

            apiLog.setStatus(response.getStatusCode().value());
            return response;
        } catch (IOException e) {
            apiLog.setException(e.getMessage());
            apiLog.setStackTrace(getStackTraceAsString(e));
            throw e;
        } finally {
            long endTime = System.nanoTime();
            apiLog.setDuration(endTime - startTime);

            apiLogRepository.save(apiLog);
        }
    }

    private String getStackTraceAsString(Exception e) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            e.printStackTrace(pw);
        }
        return sw.toString();
    }
}