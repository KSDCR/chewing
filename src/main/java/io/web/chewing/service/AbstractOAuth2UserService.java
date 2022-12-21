package io.web.chewing.service;

import io.web.chewing.Entity.Member;
import io.web.chewing.common.converters.ProviderUserConverter;
import io.web.chewing.common.converters.ProviderUserRequest;
import io.web.chewing.model.ProviderUser;
import io.web.chewing.repository.MemberRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

@Service
@Getter
@Slf4j
public abstract class AbstractOAuth2UserService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter;

    public ProviderUser providerUser(ProviderUserRequest providerUserRequest) {

        return providerUserConverter.converter(providerUserRequest);
    }

    public void register(ProviderUser providerUser, OAuth2UserRequest userRequest) {
        saveMember(providerUser, userRequest);
    }

    public Member saveMember(ProviderUser providerUser, OAuth2UserRequest userRequest) {
        return memberRepository.findByEmail(providerUser.getUsername()).orElseGet(() -> RegisterMember(providerUser, userRequest));

    }

    public Member RegisterMember(ProviderUser providerUser, OAuth2UserRequest userRequest) {
        return userService.register(providerUser);
    }


}
