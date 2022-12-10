package io.web.chewing.model;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class KakaoUser extends OAuth2ProviderUser {
    public KakaoUser(OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super((Map<String, Object>) oAuth2User.getAttributes().get("kakao_account"),
                oAuth2User,
                clientRegistration);
    }

    Map<String,Object> profile = (Map<String, Object>) getAttributes().get("profile");

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getUsername() {
        return (String) getAttributes().get("email");
    }

    @Override
    public String getProfile() {
        return (String) profile.get("image");
    }

    @Override
    public char getGender() {
        return 'k';
    }

    @Override
    public String getPhone() {
        return null;
    }

    @Override
    public String getNickName() {

        return (String) profile.get("nickname");
    }

    @Override
    public String getName() {
        return (String) profile.get("nickname");
    }
}
