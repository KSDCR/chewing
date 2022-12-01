package io.web.chewing.controller;

import io.web.chewing.Entity.Store;
import io.web.chewing.domain.StoreDto;
import io.web.chewing.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreService service;

    /*매장 정보 조회*/
    @GetMapping("/get")
    public void store(Long id, Model model) {
        // store_id로 조회
        Optional<Store> store = service.get(id);
        log.info("=========================="+String.valueOf(store));


      //  model.addAttribute("store", store);
    }

    @PostMapping("/register")
    public String register(StoreDto storeDto) throws Exception {

        service.register(storeDto);

        return "store/list";

    }
}
