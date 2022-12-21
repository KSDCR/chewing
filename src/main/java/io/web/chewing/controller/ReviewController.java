package io.web.chewing.controller;

import io.web.chewing.domain.PageInfo;
import io.web.chewing.domain.PageRequestDto;
import io.web.chewing.domain.ReviewDto;
import io.web.chewing.model.PrincipalUser;
import io.web.chewing.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("review")
@Log4j2
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class ReviewController {
    @Value("${aws.s3.file.url.prefix}")
    String imgUrl;
    private final ReviewService reviewService;


    // 수정했으니 확인할것
    @GetMapping("/myList")
    public String myList(@RequestParam(name = "page", defaultValue = "1") int page,
                         PageInfo pageInfo,
                         Model model,
                         @AuthenticationPrincipal PrincipalUser principalUser) {
        // 나중에 롤 처리 일괄로 할꺼라서 일단 임시처리
        if (principalUser.providerUser().getNickName() == null) {
            return "redirect:/login";
        }

        List<ReviewDto> list = reviewService.listReviewByMember(principalUser.providerUser().getNickName(), page, pageInfo);
        list.forEach(reviewDto -> log.info(reviewDto));

        String member_nickname = principalUser.providerUser().getNickName();
        model.addAttribute("myReviewList", list);
        model.addAttribute("member_nickname", member_nickname);
        model.addAttribute("imgUrl", imgUrl);

        log.info(list);

        return "review/myList";
    }

    @GetMapping("list")
    public void list(@RequestParam(name = "page", defaultValue = "1") int page,
                     PageInfo pageInfo,
                     String store_name,
                     Model model,
                     @AuthenticationPrincipal PrincipalUser principalUser) {

        log.info("인증객체는?" + principalUser);

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
    public void register(@AuthenticationPrincipal PrincipalUser principalUser) {
        log.info("객체가 있나요?" + principalUser);
    }

    //
    @PostMapping("register")
    public String register(@Validated ReviewDto reviewDto,
                           BindingResult bindingResult,
                           RedirectAttributes rttr,
                           MultipartFile[] files,
                           @AuthenticationPrincipal PrincipalUser principalUser,
                           String store_name) throws NotFoundException {

        log.info("POST register.......");
        log.info("인증객체는?" + principalUser);

        if (bindingResult.hasErrors()) {
            log.info("has errors......." + bindingResult.getAllErrors());
            rttr.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/review/register";
        }

        log.info(reviewDto);

        String id = reviewService.register(reviewDto, principalUser, store_name, files);

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



