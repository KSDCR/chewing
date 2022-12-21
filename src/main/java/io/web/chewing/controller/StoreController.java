package io.web.chewing.controller;

import io.web.chewing.Entity.Store;
import io.web.chewing.config.security.dto.AuthMemberDTO;
import io.web.chewing.domain.PageDto;
import io.web.chewing.domain.StoreDto;
import io.web.chewing.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Value("${aws.s3.file.url.prefix}")
    private String imgUrl;

//    @Value("${KAKAO_MAP_URL}")
//    private String kakaoMapURL;

    /*매장 정보 조회*/
    @GetMapping("/get")
    public void get(Long id, Model model,
                    @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        String nickname = "";
        if (authMemberDTO != null) { //로그인 된 경우
            nickname = authMemberDTO.getNickname();
            model.addAttribute("nickname", nickname);
            log.info("로그인 nickname ===========> " + nickname);
        }
        StoreDto storeDto = storeService.get(id, nickname);
        log.info("===========> " + storeDto);
        model.addAttribute("imgUrl", imgUrl + "/store");
        model.addAttribute("store", storeDto);

        //HttpHeaders headers = new HttpHeaders();
        //headers.add("kakaoMapURL", kakaoMapURL);
        //model.addAttribute("kakaoMapURL", kakaoMapURL);
    }

    /*매장 리스트 조회*/
    @GetMapping("/list")
    public void list(Model model, Pageable pageable,
                     @RequestParam(required = false, name = "keyword") String keyword,
                     @RequestParam(required = false, name = "category") String category,
                     @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        log.info("LIST 로그인 객체 =============> {}", String.valueOf(authMemberDTO));
        log.info("keyword =====> {} / category ======> {}", keyword, category);
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);

        // 전체 매장 리스트
        Page<StoreDto> stores;

        // 키워드 검색 매장 리스트
        if (keyword != null && keyword != "") {
            stores = storeService.listByKeyword(page, 6, keyword);
        }
        // 카테고리별 매장 리스트
        else if (category != null && category != "") {
            stores = storeService.listByCategory(page, 6, category);
        }
        // 전체 매장 리스트
        else {
            stores = storeService.list(page, 6);
        }

        PageDto paging = storeService.page(stores, keyword, category);
        //log.info("stores ================> {}", stores.stream().toList());
        log.info("paging ================> {}", paging);

        model.addAttribute("stores", stores);
        model.addAttribute("paging", paging);
        model.addAttribute("imgUrl", imgUrl + "/store");
    }

    /*매장 등록 (admin)*/
    @GetMapping("/register")
    public void register() {

    }

    @PostMapping("/register")
    public String register(
            StoreDto storeDto,
            MultipartFile image,
            RedirectAttributes rttr,
            @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        log.info(String.valueOf(authMemberDTO));
        long id = storeService.register(storeDto, image);
        log.info("register id ===========> {}", id);
        if (id > 0) {
            rttr.addFlashAttribute("message", "새 매장이 등록되었습니다.");
            log.info("======== 매장 등록 ========> {}", String.valueOf(storeDto));
        }
        return "redirect:/store/list";
    }

    /*매장 정보 수정 (admin)*/
    @GetMapping("/modify")
    public void modify(Long id, Model model, @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        StoreDto storeDto = storeService.get(id, authMemberDTO.getNickname());
        log.info("==== modify ====> " + storeDto);
        model.addAttribute("store", storeDto);
        model.addAttribute("imgUrl", imgUrl + "/store");
    }

    @PostMapping("/modify")
    public String modify(
            StoreDto storeDto,
            @RequestParam("image") MultipartFile addImage,
            @RequestParam(name = "removeImage", required = false) String removeImage,
            RedirectAttributes rttr) {
        storeService.update(storeDto, addImage, removeImage);

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

    /*매장 찜하기*/
    @PutMapping ("/like")
    @ResponseBody
    // @PreAuthorize("isAuthenticated()")
    public Map<String,Object> like(
            @RequestBody Map<String, String> req,
            @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        Map<String, Object> result = storeService.updateLike(req.get("storeName"), authMemberDTO.getNickname());
        return result;
    }

    /*찜한 매장 리스트*/
    @GetMapping("/myLike")
    public void myLike(Model model, @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        List<Store> stores = storeService.myLikeList(authMemberDTO.getNickname());
        int likeStoreCnt = stores.size();

        model.addAttribute("stores", stores);
        model.addAttribute("imgUrl", imgUrl + "/store");
        model.addAttribute("likeStoreCnt", likeStoreCnt);
    }

    /*매장 랭킹 - BEST 10*/
    @GetMapping("/rank")
    public void rank(Model model) {
        List<String> storeRank = storeService.getRank();
        log.info("storeRank ====================> {}",storeRank);
        model.addAttribute("storeRank", storeRank);
    }

}
