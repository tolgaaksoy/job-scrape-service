package io.jobmarks.jobscrapeservice.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseFilter {
    private Integer limit = 50;
    private Integer page;
    private Integer size;
    private Integer offset;

    public void nextPage() {
        if (this.page == null) {
            this.page = 1;
        } else {
            this.page += 1;
        }
    }

}
