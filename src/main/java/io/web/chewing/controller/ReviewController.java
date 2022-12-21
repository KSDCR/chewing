package io.web.chewing.controller;

import io.web.chewing.Entity.Store;
import io.web.chewing.config.security.dto.AuthMemberDTO;
import io.web.chewing.domain.PageInfo;
import io.web.chewing.domain.PageRequestDto;
import io.web.chewing.domain.ReviewDto;
import io.web.chewing.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("review")
@Log4j2
@RequiredArgsConstructor
public class ReviewController {
    @Value("${aws.s3.file.url.prefix}")
    String imgUrl;
    private final ReviewService reviewService;


    // 수정했으니 확인할것
    @GetMapping("/myList")
    public String myList(@RequestParam(name = "page", defaultValue = "1") int page,
                         PageInfo pageInfo,
                         Model model,
                         @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        // 나중에 롤 처리 일괄로 할꺼라서 일단 임시처리
        if (authMemberDTO == null) {
            return "redirect:/login";
        }

        List<ReviewDto> list = reviewService.listReviewByMember(authMemberDTO.getNickname(), page, pageInfo);
        list.forEach(reviewDto -> log.info(reviewDto));

        String member_nickname = authMemberDTO.getNickname();
        model.addAttribute("myReviewList", list);
        model.addAttribute("member_nickname", authMemberDTO.getNickname());
        model.addAttribute("imgUrl", imgUrl);

        log.info(list);

        return "review/myList";
    }

    @GetMapping("list")
    public void list(@RequestParam(name = "page", defaultValue = "1") int page,
                     PageInfo pageInfo,
                     String store_name,
                     Model model,
                     @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {

        log.info("인증객체는?" + authMemberDTO);

        List<ReviewDto> list = reviewService.listReviewByStore(store_name, page, pageInfo);
        list.forEach(reviewDto -> log.info(reviewDto));

        model.addAttribute("reviewList", list);
        model.addAttribute("store_name", store_name);
        model.addAttribute("imgUrl", imgUrl);

        log.info(imgUrl);


        log.info(":::::::::::::" + store_name);
        log.info("${imgUrl }/${review.id }/${URLEncoder.encode(file, 'utf-8')}");

    }

    @GetMapping("register")
    public void register(@AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        log.info("객체가 있나요?" + authMemberDTO);
    }

    //
    @PostMapping("register")
    public String register(@Validated ReviewDto reviewDto,
                           BindingResult bindingResult,
                           RedirectAttributes rttr,
                           MultipartFile[] files,
                           @AuthenticationPrincipal AuthMemberDTO authMemberDTO,
                           String store_name) throws NotFoundException {

        log.info("POST register.......");
        log.info("인증객체는?" + authMemberDTO);

        if (bindingResult.hasErrors()) {
            log.info("has errors......." + bindingResult.getAllErrors());
            rttr.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/review/register";
        }

        log.info(reviewDto);

        String id = reviewService.register(reviewDto, authMemberDTO, store_name, files);

        log.info("id" + id);

        rttr.addFlashAttribute("result", id);
        rttr.addFlashAttribute("store_name", store_name);

        return "redirect:/review/list?store_name=" + store_name;
    }

    @GetMapping({"remove", "modify"})
    public void findReviewById(Long id, PageRequestDto pageRequestDto, Model model) {

        ReviewDto reviewDto = reviewService.get(id);

        log.info("===================" + reviewDto);

        model.addAttribute("reviewDto", reviewDto);
        model.addAttribute("imgUrl", imgUrl);

    }


    @PostMapping("modify")
    public String updateReview(
//            PageRequestDto pageRequestDto,
            @Validated ReviewDto reviewDto,
            BindingResult bindingResult,
            RedirectAttributes rttr,
            MultipartFile[] files,
            @RequestParam(name = "removeFiles", required = false) List<String> removeFiles) {


        if (bindingResult.hasErrors()) {
            log.info("has errors.......");

//            String link = pageRequestDto.getLink();

            rttr.addFlashAttribute("errors", bindingResult.getAllErrors());

            rttr.addAttribute("id", reviewDto.getId());

            return "redirect:/list/modify?id=" + reviewDto.getId();
        }

        if (removeFiles != null) {
            for (String name : removeFiles) {
                System.out.println(name);
            }
        }

        log.info("===================" + reviewDto);

        String member_nickname = reviewService.modify(reviewDto, files, removeFiles);

        rttr.addFlashAttribute("result", "modified");


//        return "redirect:/review/read";

//        reviewService.update(review, files, removeFiles);


        return "redirect:/review/myList?member_nickname=" + member_nickname;
    }

    @PostMapping("modifybefore")
    public String updateReviewbefore(
            PageRequestDto pageRequestDto,
            ReviewDto reviewDto,
            BindingResult bindingResult,
            RedirectAttributes rttr,
            MultipartFile[] files,
            @RequestParam(name = "removeFiles", required = false) List<String> removeFiles) {


        if (bindingResult.hasErrors()) {
            log.info("has errors.......");

            String link = pageRequestDto.getLink();

            rttr.addFlashAttribute("errors", bindingResult.getAllErrors());

            rttr.addAttribute("id", reviewDto.getId());

            return "redirect:/list/modify?" + link;
        }

        reviewService.modify(reviewDto, files, removeFiles);

        rttr.addFlashAttribute("result", "modified");

        rttr.addAttribute("id", reviewDto.getId());

//        return "redirect:/review/read";

//        reviewService.update(review, files, removeFiles);

        return "redirect:/review/list";
    }


    @PostMapping("remove")
    public String deleteReview(ReviewDto reviewDto, RedirectAttributes rttr) {

        Long id = reviewDto.getId();

        log.info("aaaaaaaaaaaaa" + reviewDto);

        log.info("remove post.. " + id);

        String member_nickname = reviewService.remove(id);

        log.info("==============" + id);

//        //게시물이 삭제되었다면 첨부 파일 삭제
//        log.info(reviewDto.getFileName());
//        List<String> fileNames = reviewDto.getFileName();
//        if(fileNames != null && fileNames.size() > 0){
//            removeFiles(fileNames, id);
//        }

        rttr.addFlashAttribute("result", "removed");

        return "redirect:/review/myList?member_nickname=" + member_nickname;

    }




//    @DeleteMapping("removeFile/{id}&{fileName}")
//    public String removeFileByName(@PathVariable ReviewDto reviewDto,@PathVariable String fileName) {
//
//        Long id = reviewDto.getId();
//
//        reviewService.removeFileByName(id, fileName);
//
//        return "redirect:/review/modify?id=" + id;
//
//    }


}



