package io.web.chewing.config;

import io.web.chewing.service.CustomOAuth2UserService;
import io.web.chewing.service.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class OAuth2ClientConfig {



    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    CustomOidcUserService customOidcUserService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/static/js/**", "/static/image/**", "/static/css/**","/static/scss/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authRequest -> authRequest
                .antMatchers("/api/user").access("hasAnyRole('SCOPE_profile','SCOPE_profile_image', 'SCOPE_email','ROLE_USER')")
                .antMatchers("/api/oidc").access("hasRole('SCOPE_openid')")
                .antMatchers("/**").permitAll().anyRequest().authenticated());


        http.csrf().disable();

        http.formLogin().loginPage("/login").loginProcessingUrl("/loginProc").defaultSuccessUrl("/").permitAll();
        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                        .userService(customOAuth2UserService)
                        .oidcUserService(customOidcUserService)));

        http.logout().logoutSuccessUrl("/login");
        http.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));

        http.headers().xssProtection().and().contentSecurityPolicy("script-src 'self'");
        return http.build();
    }


}
