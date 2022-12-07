//package io.web.chewing.controller;
//
//import io.web.chewing.Entity.Store;
//import io.web.chewing.domain.PageRequestDto;
//import io.web.chewing.domain.PageResponseDto;
//import io.web.chewing.domain.ReviewDto;
//import io.web.chewing.domain.StoreDto;
//import io.web.chewing.service.StoreService;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//import java.util.Optional;
//
//@Controller
//@RequestMapping("/search")
//@Log4j2
//public class SearchController {
//
//    @Autowired
//    private StoreService storeService;
//
////    @GetMapping("/main")
////    public void search(@RequestParam(name="page", defaultValue = "1") int page,
////                       @RequestParam(name="t", defaultValue = "all") String type,
////                       @RequestParam(name="q", defaultValue = "") String keyword) {
////
////
////
////    }
//
//
////
////    @GetMapping("/main")
////    public void search(PageRequestDto pageRequestDto, Model model) {
////
////        PageResponseDto<StoreDto> responseDto = storeService.list(pageRequestDto);
////
////        log.info(responseDto);
////
////        model.addAttribute("responseDto", responseDto);
////
////
////    }
////
////    @GetMapping("/get")
////    public void read(Long id, PageRequestDto pageRequestDto, Model model){
////
////        StoreDto storeDto = storeService.findById(id);
////
////        log.info(storeDto);
////
////        model.addAttribute("dto", storeDto);
//
////    }
//}
