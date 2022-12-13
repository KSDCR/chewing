package io.web.chewing.domain;

import io.web.chewing.Entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreDto {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String detail;
    private String file;
    private String open_time;
    private String close_time;

    private String category;

    public StoreDto(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.address = store.getAddress();
        this.phone = store.getPhone();
        this.detail = store.getDetail();
        this.open_time = store.getOpen_time();
        this.close_time = store.getClose_time();

        this.file = store.getFile();
//        if(store.getFile()!=null){
//            this.file = store.getFile().getName();
//        }

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
