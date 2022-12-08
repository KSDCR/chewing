package io.web.chewing.model;

import org.springframework.security.core.GrantedAuthority;

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

    char getGender();

    String getPhone();

    String getNickName();

    List<? extends GrantedAuthority> getAuthorities();

    Map<String, Object> getAttributes();
}
