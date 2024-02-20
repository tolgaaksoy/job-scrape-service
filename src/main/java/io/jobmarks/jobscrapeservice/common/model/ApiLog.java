package io.jobmarks.jobscrapeservice.common.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "apiLogs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiLog {

    @Id
    private String id;
    private String url;
    private String method;
    private String body;
    private String headers;
    private Integer status;
    private Long duration;
    private Instant requestTimestamp;
    private Instant responseTimestamp;
    private String exception;
    private String stackTrace;
}
