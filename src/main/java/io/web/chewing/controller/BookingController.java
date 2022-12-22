package io.web.chewing.controller;

import io.web.chewing.config.security.dto.AuthMemberDTO;
import io.web.chewing.domain.BookingDTO;
import io.web.chewing.domain.PageDto;
import io.web.chewing.model.PrincipalUser;
import io.web.chewing.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

@RequiredArgsConstructor
@RequestMapping("booking")
@Controller
@Slf4j
@PreAuthorize("hasRole('USER')")
public class BookingController {
    private final BookingService bookingService;


    @GetMapping("/myList")
    public void myList(Pageable pageable, Model model,
                       @RequestParam(required = false, name =  "keyword") String keyword,
                       @AuthenticationPrincipal PrincipalUser principalUser) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);

        Page<BookingDTO> bookings = bookingService.listPage(page, 3, principalUser.providerUser().getNickName());;

        bookings.forEach(booking -> log.info("entity: " + booking));

        PageDto paging = bookingService.page(bookings, page, keyword);

        model.addAttribute("myBookingList", bookings);
        model.addAttribute("paging", paging);
    }


    @GetMapping("register")
    public String register(@AuthenticationPrincipal PrincipalUser principalUser, RedirectAttributes rttr) {
 /*       if (authMemberDTO == null) {
            return "redirect:/login";
        }*/
        return "booking/register";
    }

    @PostMapping("register")
    public String register(@Validated BookingDTO bookingDTO,
                           BindingResult bindingResult,
                           RedirectAttributes rttr,
                           @AuthenticationPrincipal PrincipalUser principalUser,
                           String store_name) throws NotFoundException {

        log.info("POST register.......");
        log.info("인증객체는?" + principalUser.providerUser());

        if (bindingResult.hasErrors()) {
            log.info("has errors......." + bindingResult.getAllErrors());
            rttr.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/booking/register";
        }

        log.info("넘어온 데이터" + String.valueOf(bookingDTO));
        log.info("넘어온 가게 이름" + store_name);

        bookingDTO.setStore_name(store_name);
        String id = String.valueOf(bookingService.register(bookingDTO, principalUser, store_name));

        log.info("id" + id);

        rttr.addFlashAttribute("result", id);
        rttr.addFlashAttribute("store_name", store_name);

        return "redirect:/booking/myList";
    }


    //
    @PostMapping("remove")
    public String deleteBooking(long id, RedirectAttributes rttr, @AuthenticationPrincipal AuthMemberDTO authMemberDTO) {

        log.info("remove post.. " + id);

        bookingService.remove(id, authMemberDTO);

        rttr.addFlashAttribute("result", "removed");

        return "redirect:/booking/Mylist";
    }
}

