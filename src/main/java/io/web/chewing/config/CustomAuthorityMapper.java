package io.web.chewing.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

// 구글
public class CustomAuthorityMapper implements GrantedAuthoritiesMapper {

    private String prefix = "ROLE_";

    @Override
    public Set<GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        HashSet<GrantedAuthority> mapped = new HashSet<>(authorities.size());
        for (GrantedAuthority authority : authorities) {
            mapped.add(mapAuthority(authority.getAuthority()));
        }
        return mapped;
    }

    private GrantedAuthority mapAuthority(String name) {

        if (name.lastIndexOf(".") > 0) {
            int index = name.lastIndexOf(".");
            name = "SCOPE_" + name.substring(index + 1);
            // EX=http://google.com/dd/dd/dd.email 이렇게 올시 .이후의 email 가져오려고하는거
        }

        if (this.prefix.length() > 0 && !name.startsWith(this.prefix)) {
            name = this.prefix + name;
        }

        return new SimpleGrantedAuthority(name);
    }
}
