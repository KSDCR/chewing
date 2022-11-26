package io.web.chewing.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class SampleController {


    @RequestMapping("/met01")
    public void Met01(){
    log.info("테스트 로그");// log.info or log.debug or log.error 등 사용 sout 사용 X


    }

}
