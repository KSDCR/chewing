package io.web.chewing.service;

import io.web.chewing.model.GoogleUser;
import io.web.chewing.model.KakaoUser;
import io.web.chewing.model.NaverUser;
import io.web.chewing.model.ProviderUser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Getter
@Slf4j
public abstract class AbstractOAuth2UserService {
    protected ProviderUser providerUser(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
        String registrationId = clientRegistration.getRegistrationId();

        return switch (registrationId){
            case "google" -> new GoogleUser(oAuth2User,clientRegistration);
            case "naver" -> new NaverUser(oAuth2User, clientRegistration);
            case "kakao" -> new KakaoUser(oAuth2User, clientRegistration);
            default -> throw new IllegalStateException("Unexpected value: " + registrationId);
        };
    }
}
