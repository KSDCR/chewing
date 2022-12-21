package io.web.chewing.model.users;

import io.web.chewing.model.ProviderUser;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class FormMember implements ProviderUser {
    private String registrationId;
    private String id;
    private String username;
    private String nickname;
    private String name;
    private String profile;
    private String gender;
    private String phone;
    private String password;
    private String email;
    private boolean isCertificated;
    private String provider;
    private List<? extends GrantedAuthority> authorities;


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getProvider() {
        return provider;
    }

    @Override
    public String getProfile() {
        return profile;
    }

    @Override
    public String getGender() {
        return gender;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getNickName() {
        return nickname;
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public OAuth2User getOAuth2User() {
        return null;
    }
}
