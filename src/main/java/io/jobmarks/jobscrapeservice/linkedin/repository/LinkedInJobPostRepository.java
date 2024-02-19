package io.jobmarks.jobscrapeservice.linkedin.repository;

import io.jobmarks.jobscrapeservice.linkedin.model.LinkedInJobPost;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LinkedInJobPostRepository extends MongoRepository<LinkedInJobPost, String> {
    Optional<LinkedInJobPost> findByExternalId(String externalId);

    boolean existsByExternalId(String externalId);
}