package io.jobmarks.jobscrapeservice.common.repository;

import io.jobmarks.jobscrapeservice.common.model.ApiLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiLogRepository extends MongoRepository<ApiLog, String> {
}
