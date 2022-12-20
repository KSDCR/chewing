package io.web.chewing.domain;

import lombok.Builder;
import lombok.Data;

@Data
public class PageDto {

    int number; // 현재페이지번호
    int start; // 페이지네이션 첫페이지 번호
    int last; // 페이지네이션 마지막페이지 번호
    int totalPages; // 전체페이지번호

    boolean hasPrevButton; // 이전 버튼 유무
    boolean hasNextButton; // 다음 버튼 유무

    int jumpPrevPageNum; // 이전 버튼 눌렀을 때 가는 페이지 번호
    int jumpNextPageNum; // 다음 버튼 눌렀을 때 가는 페이지 번호

    String keyword; // 검색 키워드
    String category; // 카테고리

    @Builder
    public PageDto(int total, int number, String keyword, String category) {
        this.totalPages = total;
        this.number = number +1;
        this.start = (int) Math.floor(number / 10) * 10 + 1;
        this.last = start + 9 < totalPages ? start + 9 : totalPages;

        this.hasPrevButton = number+1 > 10;
        this.hasNextButton = number+1 <= ((last) / 10 * 10);

        this.jumpPrevPageNum = (number) / 10 * 10;
        this.jumpNextPageNum = (number) / 10 * 10 + 11;

        this.keyword = keyword;
        this.category = category;

        System.out.println("========> number: " + number + "/ last: " + last + "/ NextButton: " + ((last - 1) / 10 * 10) );
    }


}
