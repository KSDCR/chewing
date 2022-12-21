package io.web.chewing.config.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Log4j2
@Setter
@Getter
@ToString
public class AuthMemberDTO extends User implements OAuth2User {

    private Long id;
    private String email;
    private String password;
    private String nickname;

    private String name;
    private String provider;

    private char delete_yn;
    private boolean verify;
    private Map<String, Object> attr;

    private List Authorities;


    public AuthMemberDTO(Long id, String username,
                         String password,
                         String nickname,
                         String provider,
                         String name,
                         char delete_yn,
                         boolean verify,
                         Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
        this.email = username;
        this.password = password;
        this.nickname = nickname;
        this.provider = provider;
        this.name = name;
        this.verify = verify;
        this.Authorities = Arrays.asList(authorities.toArray());
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
