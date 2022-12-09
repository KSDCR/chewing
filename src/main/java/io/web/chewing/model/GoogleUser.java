package io.web.chewing.model;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class GoogleUser extends OAuth2ProviderUser{

    public GoogleUser(OAuth2User oAuth2User, ClientRegistration clientRegistration){
        super(oAuth2User.getAttributes(),oAuth2User,clientRegistration);
    }

    @Override
    public String getId() {
        return (String) getAttributes().get("sub");
    }

    @Override
    public String getUsername() {
        return (String) getAttributes().get("email");
    }

    @Override
    public String getProfile() {
        return (String) getAttributes().get("picture");
    }

    @Override
    public char getGender() {
        return 'g';
    }

    @Override
    public String getPhone() {
        return null;
    }

    @Override
    public String getNickName() {
        return (String) (getAttributes().get("name"));
    }

    @Override
    public String getName() {
        return (String) getAttributes().get("name");
    }
}
