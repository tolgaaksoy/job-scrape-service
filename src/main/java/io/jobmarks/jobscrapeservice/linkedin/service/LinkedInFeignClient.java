package io.jobmarks.jobscrapeservice.linkedin.service;

import feign.Headers;
import io.jobmarks.jobscrapeservice.common.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "linkedInClient", url = "https://www.linkedin.com", configuration = FeignConfig.class)
public interface LinkedInFeignClient {

    @GetMapping("/jobs-guest/jobs/api/seeMoreJobPostings/search")
    @Headers({
            "authority: www.linkedin.com",
            "accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
            "accept-language: en-US,en;q=0.9",
            "cache-control: max-age=0",
            "sec-ch-ua: \"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"",
            "upgrade-insecure-requests: 1",
            "user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
            // Uncomment the following lines if needed
            // "sec-ch-ua-mobile: ?0",
            // "sec-ch-ua-platform: \"macOS\"",
            // "sec-fetch-dest: document",
            // "sec-fetch-mode: navigate",
            // "sec-fetch-site: none",
            // "sec-fetch-user: ?1",
    })
    String getJobs(@RequestParam("keywords") String keywords,
                   @RequestParam("location") String location,
                   @RequestParam("f_WT") String remote,
                   @RequestParam("f_JT") String jobType,
                   @RequestParam("f_AL") Boolean easyApply,
                   @RequestParam("f_C") String companyIds,
                   @RequestParam("f_TPR") String secondsOld,
                   @RequestParam("pageNum") Integer page,
                   @RequestParam("start") Integer start);

    @GetMapping("/jobs/view/{externalId}")
    @Headers({
            "authority: www.linkedin.com",
            "accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
            "accept-language: en-US,en;q=0.9",
            "cache-control: max-age=0",
            "sec-ch-ua: \"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"",
            "upgrade-insecure-requests: 1",
            "user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
            // Uncomment the following lines if needed
            // "sec-ch-ua-mobile: ?0",
            // "sec-ch-ua-platform: \"macOS\"",
            // "sec-fetch-dest: document",
            // "sec-fetch-mode: navigate",
            // "sec-fetch-site: none",
            // "sec-fetch-user: ?1",
    })
    String getJob(@PathVariable("externalId") String externalId);
}
