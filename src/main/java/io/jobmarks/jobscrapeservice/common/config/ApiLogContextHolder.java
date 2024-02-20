package io.jobmarks.jobscrapeservice.common.config;

import io.jobmarks.jobscrapeservice.common.model.ApiLog;

public class ApiLogContextHolder {

    private ApiLogContextHolder() {
        throw new IllegalStateException("Utility class");
    }

    private static final ThreadLocal<ApiLog> apiLogThreadLocal = new ThreadLocal<>();

    public static void setApiLog(ApiLog apiLog) {
        apiLogThreadLocal.set(apiLog);
    }

    public static ApiLog getApiLog() {
        return apiLogThreadLocal.get();
    }

    public static void clear() {
        apiLogThreadLocal.remove();
    }
}