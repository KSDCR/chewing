package io.web.chewing.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HomeController {
    // 현재 구글 같은 경우엔 scope가 email = email profile = profile 이런 형태가 아니고 http로 시작하는 아주 긴 문장으로 보내줌
    // 그래서 필터링 하는 작업을 해주는 CustomAuthorityMapper필요함
    @GetMapping("/api/user")
    public Authentication user(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2User) {
        log.info("Controller authentication = " + authentication + ", oAuth2User = " + oAuth2User);
        return authentication;
    }
    // 네이버는 oidc provider 요청없이 해당 방식으로 인증 불가능하기 때문에 여기로 접속이 불가능함(권한 역시 없음)
    @GetMapping("/api/oidc")
    public Authentication user(Authentication authentication, @AuthenticationPrincipal OidcUser oidcUser) {
        log.info("authentication = " + authentication + ", oidcUser = " + oidcUser);
        return authentication;
    }
}
