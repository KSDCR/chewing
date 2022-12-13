package io.web.chewing.config;

import io.web.chewing.service.CustomOAuth2UserService;
import io.web.chewing.service.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class OAuth2ClientConfig {

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    CustomOidcUserService customOidcUserService;

/*    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().antMatchers("/static/js/**","/static/images/**","/static/css/**","/static/scss/**");
    }*/

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authRequest -> authRequest
                .antMatchers("/api/user").access("hasAnyRole('SCOPE_profile','SCOPE_profile_image', 'SCOPE_email','ROLE_USER')")
                .antMatchers("/api/oidc").access("hasRole('SCOPE_openid')")
                .antMatchers("/").permitAll().anyRequest().authenticated());

        http.csrf().disable();
        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                        .userService(customOAuth2UserService)
                        .oidcUserService(customOidcUserService)));

        http.logout().logoutSuccessUrl("/login");
        http.formLogin().successForwardUrl("/");
        return http.build();
    }

    @Bean
    public GrantedAuthoritiesMapper customAuthoritiesMapper(){
        return new CustomAuthorityMapper();
    }
}
