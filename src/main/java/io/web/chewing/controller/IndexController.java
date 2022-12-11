package io.web.chewing.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
@Slf4j
public class IndexController {

    @GetMapping("/")
    public String index(Model model, Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2User){
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        if(oAuth2AuthenticationToken != null){
            Map<String,Object> attributes = oAuth2User.getAttributes();
            attributes.forEach((s, o) -> log.info("Map뭐 들었나?" + s + ":" + o));
            String name = (String) attributes.get("name");

            if (oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals("naver")){
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                name = (String) response.get("nickname");
            }else if(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals("kakao")){
                Map<String,Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
                Map<String,Object> profile = (Map<String, Object>) kakao_account.get("profile");
                name = (String) profile.get("nickname");
            }
            log.info("제대로 가져오나?"+name);
            model.addAttribute("user",name);
        }

        return "index";
    }
}
