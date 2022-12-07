package io.web.chewing.domain;

import lombok.Builder;
import lombok.Data;

@Data
public class PageDto {
    int start;
    int last;
    int totalPages;
    int number;
    String keyword;
    String category;

    @Builder
    public PageDto(int total, int number, String keyword, String category){
        this.totalPages = total;
        this.number = number;
        this.start = (int) Math.floor(number/10)*10+1;
        this.last = start+9 < totalPages ? start+9 : totalPages;

        this.keyword = keyword;
        this.category = category;
    }
}
