package io.web.chewing.domain;

import io.web.chewing.Entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StoreDto {
    // 이 파일이 이상해서 커밋하려고 적은 내용
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String detail;
    private String file;
    private String open_time;
    private String close_time;

    private String category;

    private Double rate;
    private int reviewCnt;

    private boolean liked;
    private int countLike;
    public StoreDto(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.address = store.getAddress();
        this.phone = store.getPhone();
        this.detail = store.getDetail();
        this.open_time = store.getOpen_time();
        this.close_time = store.getClose_time();
        this.category = store.getCategory();

        this.file = store.getFile();
    }

    /* Dto -> Entity
    public Store toEntity() {
        Store store = Store.builder()
                .id(id)
                .name(name)
                .address(address)
                .phone(phone)
                .detail(detail)
                .file(file)
                .open_time(open_time)
                .close_time(close_time)
                .build();

        return store;
    }*/
}
