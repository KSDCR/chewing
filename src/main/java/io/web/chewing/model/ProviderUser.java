package io.web.chewing.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;

public interface ProviderUser {

    String getId();

    String getUsername();

    String getPassword();

    String getName();

    String getEmail();

    String getProvider();

    String getProfile();

    String getGender();

    String getPhone();

    String getNickName();

    List<? extends GrantedAuthority> getAuthorities();

    Map<String, Object> getAttributes();

    OAuth2User getOAuth2User();
}
