package io.web.chewing.model;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class KakaoUser extends OAuth2ProviderUser {
    public KakaoUser(OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(oAuth2User.getAttributes(), oAuth2User, clientRegistration);
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public String getProfile() {
        return null;
    }

    @Override
    public String getGender() {
        return null;
    }

    @Override
    public String getPhone() {
        return null;
    }

    @Override
    public String getNickName() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
