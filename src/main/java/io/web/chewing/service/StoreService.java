package io.web.chewing.service;

import io.web.chewing.Entity.Categories;
import io.web.chewing.Entity.Store;
import io.web.chewing.domain.PageRequestDto;
import io.web.chewing.domain.PageResponseDto;
import io.web.chewing.domain.StoreDto;
import io.web.chewing.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class StoreService {

    @Autowired
    private StoreRepository repo;

    public Optional<Store> get(Long id) {
        return repo.findById(id);
    }

    public void register(StoreDto dto) {

        Store store = Store.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .detail(dto.getDetail())
                .openTime(String.valueOf(LocalDateTime.now()))
                .closeTime(String.valueOf(LocalDateTime.now()))
                .file("fileNotFound")
                .build();

        store.addCategories(Categories.Korean);

        /*Store result = repo.save(store);*/


        repo.save(store);
    }


}