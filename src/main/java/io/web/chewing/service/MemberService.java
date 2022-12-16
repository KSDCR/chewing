package io.web.chewing.service;


import io.web.chewing.Entity.Member;
import io.web.chewing.model.ProviderUser;
import io.web.chewing.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
public class MemberService {
    private final ModelMapper modelMapper;

    private final MemberRepository memberRepository;

    public Member register(String registrationId, ProviderUser providerUser ){
        Member member = Member.builder()
                .email(providerUser.getUsername())
                .password(providerUser.getPassword())
                .provider(providerUser.getProvider())
                .nickname(providerUser.getNickName())
                .name(providerUser.getName())
                .phone(providerUser.getPhone())
                .gender(providerUser.getGender())
        .build();

        return member;
    }

}
