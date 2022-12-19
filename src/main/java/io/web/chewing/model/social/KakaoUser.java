package io.web.chewing.model.social;

import io.web.chewing.model.Attributes;
import io.web.chewing.model.OAuth2ProviderUser;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class KakaoUser extends OAuth2ProviderUser {

    private Map<String, Object> otherAttributes;

    public KakaoUser(Attributes attributes, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(attributes.getSubAttributes(),
                oAuth2User,
                clientRegistration);
        this.otherAttributes = attributes.getOtherAttributes();
    }



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
        return (String) otherAttributes.get("profile_image_url");
    }

    @Override
    public String getGender() {
        return (String) otherAttributes.get("gender");
    }

    @Override
    public String getPhone() {
        return null;
    }

    @Override
    public String getNickName() {

        return "K_"+(String) otherAttributes.get("nickname");
    }

    @Override
    public String getName() {
        return (String) otherAttributes.get("nickname");
    }
}
