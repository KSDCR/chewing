package io.web.chewing.controller;

import io.web.chewing.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping
@Controller
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    /**
     * 예약가능 매장 조회
     */
    /*@GetMapping("/store")
    public String storeList(
            // 페이징 처리
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "10") int limit, Model model,
            @AuthenticationPrincipal PrincipalDetails user) {

        List<ListDto> hospitalListDtos = reserveItemService.getAllHospitalInfo(offset, limit);
        model.addAttribute("hospitalList", hospitalListDtos);
        return "user/reserve/hospitalList";
    }*/

}
