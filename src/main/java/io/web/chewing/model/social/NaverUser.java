package io.web.chewing.model.social;

import io.web.chewing.model.Attributes;
import io.web.chewing.model.OAuth2ProviderUser;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class NaverUser extends OAuth2ProviderUser {
    public NaverUser(Attributes attributes, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(attributes.getSubAttributes(),
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
        return (String) getAttributes().get("profile_image");
    }

    @Override
    public String getGender() {
        return (String) getAttributes().get("gender");
    }

    @Override
    public String getPhone() {
        return (String) getAttributes().get("mobile");
    }

    @Override
    public String getNickName() {
        return "N_"+(String) getAttributes().get("nickname");
    }

    @Override
    public String getName() {
        return (String) getAttributes().get("name");
    }
}
