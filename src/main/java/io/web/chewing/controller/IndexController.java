package io.web.chewing.controller;

import io.web.chewing.common.util.OAuth2Utils;
import io.web.chewing.model.PrincipalUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Map;

@Controller
@Slf4j
public class IndexController {

    @GetMapping("/")
    public String index(Model model, Authentication authentication, @AuthenticationPrincipal PrincipalUser principalUser) {

        if (authentication != null) {
            String name;
            if (authentication instanceof OAuth2AuthenticationToken) {
                name = OAuth2Utils.OAuth2UserName((OAuth2AuthenticationToken) authentication, principalUser);
            } else {
                name = principalUser.providerUser().getUsername();
            }
            log.info("principalUser 확인: " + principalUser);
            log.info("나오나여?"+principalUser.providerUser().getNickName());

            model.addAttribute("user", name);
            model.addAttribute("provider", principalUser.providerUser().getProvider());
        }
        return "index";
    }
}
