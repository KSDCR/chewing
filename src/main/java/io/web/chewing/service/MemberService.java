package io.web.chewing.service;


import io.web.chewing.Entity.Member;
import io.web.chewing.model.ProviderUser;
import io.web.chewing.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class MemberService {


    private final MemberRepository memberRepository;

    public Member register(String registrationId, ProviderUser providerUser ){

        Member member = Member.builder()
                .name(providerUser.getNickName())
                .build();
        return member;
    }

}
