package io.web.chewing.controller;

import io.web.chewing.domain.StoreDto;
import io.web.chewing.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/")
public class MainController {

    @Autowired
    private StoreService storeService;

    @Value("${aws.s3.file.url.prefix}")
    private String imgUrl;

    @GetMapping("/main")
    public void main(Model model){
        /*랜덤 매장 보여주기*/
        List<StoreDto> storeRandom = storeService.getRandomStore();
        log.info("storeRandom ====================> {}",storeRandom);
        model.addAttribute("storeRandom", storeRandom);
        model.addAttribute("imgUrl", imgUrl + "/store");
    }
}
