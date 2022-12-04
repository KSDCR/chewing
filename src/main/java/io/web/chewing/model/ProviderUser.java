package io.web.chewing.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;

public interface ProviderUser {

    String getId();
    String getUsername();
    String getPassword();
    String getEmail();
    String getProvider();
    String getProfile();
    String getGender();
    String getPhone();

    List<? extends GrantedAuthority> getAuthorities();
    Map<String,Object> getAttributes();
}
