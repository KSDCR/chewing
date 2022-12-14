package io.web.chewing.controller;

import io.web.chewing.Entity.Store;
import io.web.chewing.config.SecurityConfig;
import io.web.chewing.config.security.dto.AuthMemberDTO;
import io.web.chewing.domain.PageInfo;
import io.web.chewing.domain.PageRequestDto;
import io.web.chewing.domain.PageResponseDto;
import io.web.chewing.domain.ReviewDto;
import io.web.chewing.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("review")
@Log4j2
@RequiredArgsConstructor
public class ReviewController {
    @Value("${aws.s3.file.url.prefix}")
    String imgUrl;
    private final ReviewService reviewService;

    @GetMapping("/myList")
    public void myList(@RequestParam(name="page", defaultValue = "1") int page,
                       PageInfo pageInfo,
                       String member_nickname,
                       Model model) {



        List<ReviewDto> list = reviewService.listReviewByMember(member_nickname, page, pageInfo);

        model.addAttribute("myReviewList", list);

        log.info(list);


    }
    @GetMapping("list")
    public void list(@RequestParam(name="page", defaultValue = "1") int page,
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


        log.info(":::::::::::::"+store_name);
        log.info("${imgUrl }/${review.id }/${URLEncoder.encode(file, 'utf-8')}");

    }
    /*complete*/


//    @GetMapping("/listbefore")
//    public void list(Long store, PageRequestDto pageRequestDto, Model model){
//
//        String member = "";
//
//        PageResponseDto<ReviewDto> responseDto = reviewService.list(store,/* member,*/ pageRequestDto);
//
//        log.info(responseDto);
//
//        model.addAttribute("responseDto", responseDto);
//
//    }

    @GetMapping("register")
    public void register(@AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        log.info("객체가 있나요?"+ authMemberDTO);
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

        return "redirect:/review/list?store_name="+store_name;
    }

    @GetMapping({"remove", "modify"})
    public void findReviewById(Long id, PageRequestDto pageRequestDto, Model model) {

        ReviewDto reviewDto = reviewService.get(id);

        log.info(reviewDto);

        model.addAttribute("dto", reviewDto);

    }


    @PostMapping("modify")
    public String updateReview(
            PageRequestDto pageRequestDto,
            @Validated ReviewDto reviewDto,
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

        if (removeFiles != null) {
            for (String name : removeFiles) {
                System.out.println(name);
            }
        }

        reviewService.modify(reviewDto, files, removeFiles);

        rttr.addFlashAttribute("result", "modified");

        rttr.addAttribute("id", reviewDto.getId());

//        return "redirect:/review/read";

//        reviewService.update(review, files, removeFiles);

        return "redirect:/review/list";
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

    //
    @PostMapping("remove")
    public String deleteReview(long id, RedirectAttributes rttr) {

        log.info("remove post.. " + id);

        reviewService.remove(id);

        rttr.addFlashAttribute("result", "removed");

        return "redirect:/review/list";
    }
}



