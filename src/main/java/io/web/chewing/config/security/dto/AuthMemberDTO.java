package io.web.chewing.config.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Log4j2
@Setter
@Getter
@ToString
public class AuthMemberDTO extends User implements OAuth2User {

    private String email;
    private String password;
    private String nickname;

    private String name;
    private String provider;

    private char delete_yn;
    private boolean verify;
    private Map<String, Object> attr;


    public AuthMemberDTO(String username,
                         String password,
                         String nickname,
                         String provider,
                         String name,
                         char delete_yn,
                         boolean verify,
                         Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.email = username;
        this.password = password;
        this.nickname = nickname;
        this.provider = provider;
        this.name = name;
        this.verify = verify;
    }


    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }


    @Override
    public String getName() {
        return this.email;
    }
}
