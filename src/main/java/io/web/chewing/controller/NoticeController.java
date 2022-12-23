package io.web.chewing.controller;

import io.web.chewing.Entity.Notice;
import io.web.chewing.domain.NoticeDTO;
import io.web.chewing.domain.PageDto;
import io.web.chewing.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@PreAuthorize("hasRole('USER')")
@RequestMapping("/notice")
@Controller
@Slf4j
public class NoticeController {

    @Autowired

    private NoticeService noticeService;

    @GetMapping("/register")
    public void registerGET() {

    }

    @PostMapping("/register")
    public String register(NoticeDTO noticeDTO) {
        log.info("NoticeController register");
        log.info("이게 깨지나요?" + noticeDTO);
        noticeService.register(noticeDTO);

        return "redirect:/notice/list";

    }

    @GetMapping("/list")
    public String list(Model model, Pageable pageable,
                     @RequestParam(required = false, name = "keyword") String keyword
                     ) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        Page<NoticeDTO> notice = noticeService.listPage(page, 4);
        PageDto paging = noticeService.page(notice, page, keyword);

        model.addAttribute("notice", notice);
        model.addAttribute("paging", paging);

        return "notice/list";
    }

    @PostMapping("/modify")
    public String modify(@RequestParam(name = "id") Long id,
                         RedirectAttributes redirectAttributes, Notice notice, NoticeDTO noticeDTO) {
        log.info("noticeDTO" + noticeDTO);

        noticeService.modify(noticeDTO);
        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("id", notice.getId());
        return "redirect:/notice/get?id=" + id;
    }

    @PostMapping("/delete")
    public String delete(@RequestParam(name = "id") Long id, RedirectAttributes redirectAttributes) {
        log.info("delete post..." + id);
        noticeService.delete(id);
        redirectAttributes.addFlashAttribute("result", "deleted");
        return "redirect:/notice/list";
    }

    @GetMapping({"/get", "/modify"})
    public void get(@RequestParam(name = "id") Long id, Model model) {
        Notice notice = noticeService.get(id);
        System.out.println(notice);

        model.addAttribute("notice", notice);
        // model.addAttribute("notice", noticeDTO);
    }
}
