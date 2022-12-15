package io.web.chewing.controller;

import io.web.chewing.Entity.Store;
import io.web.chewing.domain.PageDto;
import io.web.chewing.domain.StoreDto;
import io.web.chewing.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreService storeService;

    private final String imgUrl = "https://study-2022-08-02-343566579.s3.ap-northeast-2.amazonaws.com/chewing/store";

    /*매장 정보 조회*/
    @GetMapping("/get")
    public void get(Long id, Model model) {
        StoreDto storeDto = storeService.get(id);
        log.info("===========> " + storeDto);

        model.addAttribute("imgUrl", imgUrl);
        model.addAttribute("store", storeDto);
    }

    /*매장 리스트 조회*/
    @GetMapping("/list")
    public void list(Model model, Pageable pageable,
                     @RequestParam(required = false, name = "keyword") String keyword,
                     @RequestParam(required = false, name = "category") String category) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        log.info("pageable ================> {}", pageable);
        log.info("keyword ================> {}", keyword);
        log.info("category ================> {}", category);
        // 전체 매장 리스트
        Page<StoreDto> stores = storeService.list(page, 6);
        
        // 키워드 검색 매장 리스트
        if (keyword != null && keyword != "") {
            stores = storeService.listByKeyword(page, 6, keyword);
        }
        
        // 카테고리별 매장 리스트
        if (category != null && category != "") {
            stores = storeService.listByCategory(page, 6, category);
        }

        PageDto paging = storeService.page(stores, keyword, category);
        log.info("stores ================> {}", stores.stream().toList());
        log.info("paging ================> {}", paging);
        
        // 매장 이미지 없을 경우 -> 기본이미지
        //String imgUrl = "https://study-2022-08-02-343566579.s3.ap-northeast-2.amazonaws.com/chewing/store/noStore/noStore.jfif";
        
        log.info("imgUrl ================> {}", imgUrl);

        model.addAttribute("stores", stores);
        model.addAttribute("paging", paging);
        model.addAttribute("imgUrl", imgUrl);
    }

    /*매장 등록 (admin)*/
    @GetMapping("/register")
    public void register() {

    }

    @PostMapping("/register")
    public String register(StoreDto storeDto, MultipartFile image, RedirectAttributes rttr) {
        long id = storeService.register(storeDto, image);
        log.info("register id ===========> {}", id);
        if (id > 0) {
            rttr.addFlashAttribute("message", "새 매장이 등록되었습니다.");
            log.info("------------ 매장 등록 ----------");
        }
        return "redirect:/store/list";
    }
    
    /*매장 정보 수정 (admin) - 추가 예정*/
    @GetMapping("/modify")
    public void modify(Long id, Model model) {
        StoreDto storeDto = storeService.get(id);
        log.info("==== modify ====> " + storeDto);
        model.addAttribute("store", storeDto);
        model.addAttribute("imgUrl", imgUrl);
    }

    @PostMapping("/modify")
    public String modify(
            StoreDto storeDto,
            @RequestParam("image") MultipartFile addImage,
            @RequestParam(name = "removeImage", required = false) String removeImage,
            RedirectAttributes rttr) {
        storeService.update(storeDto, addImage, removeImage);

        //if ()
        return "redirect:/store/get?id=" + storeDto.getId();
    }
    
    /*매장 정보 삭제 (admin)*/
    @PostMapping("/remove")
    public String remove(Long id) {
        storeService.remove(id);
        return "redirect:/store/list";
    }
    
    /*매장명 중복 확인*/
    @GetMapping("existName/{name}")
    @ResponseBody
    public Map<String, Object> existName(@PathVariable String name) {
        Map<String, Object> map = new HashMap<>();
        Store store = storeService.getByName(name);

        if (store == null) {
            map.put("status", "not exist");
            map.put("message", "사용 가능한 매장명입니다.");
        } else {
            map.put("status", "exist");
            map.put("message", "이미 존재하는 매장명입니다.");
        }
        return map;
    }
}
