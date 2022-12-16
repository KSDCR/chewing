package io.web.chewing.domain;

import lombok.Data;

@Data
public class PageInfo {
    private int lastPageNumber;
    private double leftPageNumber;
    private double rightPageNumber;
    private int currentPageNumber;
    private boolean hasNextPageNumber;
}
