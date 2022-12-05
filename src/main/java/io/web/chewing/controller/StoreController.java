package io.web.chewing.controller;

import io.web.chewing.Entity.Store;
import io.web.chewing.domain.PageRequestDto;
import io.web.chewing.domain.PageResponseDto;
import io.web.chewing.domain.StoreDto;
import io.web.chewing.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreService storeService;

    /*매장 정보 조회*/
    @GetMapping("/get")
    public void get(Long id, Model model) {
        StoreDto storeDto = storeService.get(id);
        log.info("===========> " + storeDto);
        model.addAttribute("store", storeDto);
    }

    /*매장 리스트 조회*/
    @GetMapping("/list")
    public void list(Model model) {
        List<StoreDto> storeList = storeService.list();
        log.info("===========> " + storeList);
        model.addAttribute("storeList", storeList);
    }
//    public void list(PageRequestDto pageRequestDto, Model model) {
//        PageResponseDto<StoreDto> responseDto = storeService.list(pageRequestDto);
//        log.info("===========> " + responseDto);
//
//        model.addAttribute("responseDto", responseDto);
//    }


    /*매장 등록 (admin) - 추가 예정*/
    @GetMapping("/register")
    public void register() {

    }

    @PostMapping("/register")
    public String register(StoreDto storeDto) {
        storeService.register(storeDto);
        log.info("===========> " + storeDto);
        return "redirect:/store/get";
    }
    
    /*매장 정보 수정 (admin) - 추가 예정*/
    @GetMapping("/modify")
    public void modify(Long id, Model model) {
        StoreDto storeDto = storeService.get(id);
    }

    @PostMapping("/modify")
    public String modify(StoreDto storeDto) {
        storeService.update(storeDto);
        return "redirect:/store/list";
    }
    
    /*매장 정보 삭제 (admin) - 추가 예정*/
    @PostMapping("/remove")
    public String remove(Long id) {
        storeService.remove(id);
        return "redirect:/store/list";
    }
}
