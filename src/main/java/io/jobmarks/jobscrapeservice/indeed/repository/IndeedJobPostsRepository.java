package io.jobmarks.jobscrapeservice.indeed.repository;

import io.jobmarks.jobscrapeservice.indeed.model.IndeedJobPost;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IndeedJobPostsRepository extends MongoRepository<IndeedJobPost, String> {
    boolean existsByExternalId(String externalId);

}
