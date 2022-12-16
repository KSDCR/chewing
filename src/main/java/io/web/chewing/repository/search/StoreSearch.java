package io.web.chewing.repository.search;

import io.web.chewing.Entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreSearch {
    Page<Store> search1(Pageable pageable);

    Page<Store> searchAll(/*String[] types,*/ String keyword, Pageable pageable);
}



