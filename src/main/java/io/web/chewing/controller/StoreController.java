package io.web.chewing.controller;

import io.web.chewing.Entity.Store;
import io.web.chewing.domain.PageDto;
import io.web.chewing.domain.PageRequestDto;
import io.web.chewing.domain.PageResponseDto;
import io.web.chewing.domain.StoreDto;
import io.web.chewing.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
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
    public void list(Model model, Pageable pageable,
                     @RequestParam(required = false, name = "keyword") String keyword,
                     @RequestParam(required = false, name = "category") String category) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("여기에 뭐가 들었나용?"+String.valueOf(authentication));

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        log.info("pageable ================> {}", pageable);

        // 전체 매장 리스트
        Page<StoreDto> stores = storeService.list(page, 5);

        // 키워드 검색 매장 리스트
        if (keyword != null) {
            stores = storeService.listByKeyword(page, 5, keyword);
        }

        // 카테고리별 매장 리스트
        /*if (category != null) {
            stores = storeService.listByCategory(page, 5, category);
        }*/

        PageDto paging = storeService.page(stores, keyword, category);
        log.info("stores ================> {}", stores.stream().toList());
        log.info("paging ================> {}", paging);

        model.addAttribute("stores", stores);
        model.addAttribute("paging", paging);
    }



    /*매장 등록 (admin) - 추가 예정*/
    @GetMapping("/register")
    public void register() {

    }

    @PostMapping("/register")
    public String register(StoreDto storeDto, MultipartFile multipartFile, RedirectAttributes rttr) {
        long id = storeService.register(storeDto, multipartFile);
        log.info("register ===========> " + storeDto);
//        if (id != null) {
            rttr.addFlashAttribute("message", "새 매장이 등록되었습니다.");
//        }
        return "redirect:/store/list";
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
