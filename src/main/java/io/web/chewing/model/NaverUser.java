package io.web.chewing.model;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class NaverUser extends OAuth2ProviderUser {
    public NaverUser(OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super((Map<String, Object>) oAuth2User.getAttributes().get("response"),
                oAuth2User,
                clientRegistration);
    }

    @Override
    public String getId() {
        return (String) getAttributes().get("id");
    }

    @Override
    public String getUsername() {
        return (String) getAttributes().get("email");
    }

    @Override
    public String getProfile() {
        return (String) getAttributes().get("nickname");
    }

    @Override
    public String getGender() {
        return "t";
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
