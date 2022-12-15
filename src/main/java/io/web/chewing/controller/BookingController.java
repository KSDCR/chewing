package io.web.chewing.controller;

import io.web.chewing.config.security.dto.AuthMemberDTO;
import io.web.chewing.domain.BookingDTO;
import io.web.chewing.domain.PageInfo;
import io.web.chewing.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("booking")
@Controller
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myList")
    public void myList(@RequestParam(name = "page", defaultValue = "1") int page,
                       PageInfo pageInfo,
                       String member_nickname,
                       Model model,
                       @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        List<BookingDTO> list = bookingService.listBookingByMember(authMemberDTO.getNickname(), page, pageInfo);

        model.addAttribute("myBookingList", list);
    }


    @GetMapping("register")
    public String register(@AuthenticationPrincipal AuthMemberDTO authMemberDTO, RedirectAttributes rttr) {
 /*       if (authMemberDTO == null) {
            return "redirect:/login";
        }*/
        return "booking/register";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("register")
    public String register(@Validated BookingDTO bookingDTO,
                           BindingResult bindingResult,
                           RedirectAttributes rttr,
                           @AuthenticationPrincipal AuthMemberDTO authMemberDTO,
                           String store_name) throws NotFoundException {

        log.info("POST register.......");
        log.info("인증객체는?" + authMemberDTO);

        if (bindingResult.hasErrors()) {
            log.info("has errors......." + bindingResult.getAllErrors());
            rttr.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/booking/register";
        }

        log.info("넘어온 데이터" + String.valueOf(bookingDTO));
        log.info("넘어온 가게 이름" + store_name);

        String id = String.valueOf(bookingService.register(bookingDTO, authMemberDTO, store_name));

        log.info("id" + id);

        rttr.addFlashAttribute("result", id);
        rttr.addFlashAttribute("store_name", store_name);

        return "redirect:/booking/myList";
    }


    //
    @PostMapping("remove")
    public String deleteBooking(long id, RedirectAttributes rttr) {

        log.info("remove post.. " + id);

        bookingService.remove(id);

        rttr.addFlashAttribute("result", "removed");

        return "redirect:/booking/Mylist";
    }
}

