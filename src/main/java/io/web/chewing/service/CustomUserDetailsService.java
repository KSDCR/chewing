package io.web.chewing.service;

import io.web.chewing.Entity.Member;
import io.web.chewing.common.converters.ProviderUserRequest;
import io.web.chewing.model.PrincipalUser;
import io.web.chewing.model.ProviderUser;
import io.web.chewing.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService extends AbstractOAuth2UserService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username).orElseThrow(() -> new IllegalArgumentException("no such data"));

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(member);

        ProviderUser providerUser = providerUser(providerUserRequest);

        return new PrincipalUser(providerUser);
    }
}
