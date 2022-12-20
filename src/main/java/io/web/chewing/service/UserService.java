package io.web.chewing.service;

import io.web.chewing.Entity.Member;
import io.web.chewing.Entity.MemberRole;
import io.web.chewing.model.ProviderUser;
import io.web.chewing.repository.MemberRepository;
import io.web.chewing.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private MemberRepository memberRepository;

    public Member register(ProviderUser providerUser){
        log.info("여기 오나요?" + providerUser);
        Member  member = Member.builder().email(providerUser.getEmail())
                .nickname(providerUser.getNickName())
                .delete_yn('0')
                .phone(providerUser.getPhone())
                .gender(providerUser.getGender())
                .provider(providerUser.getProvider())
                .password(providerUser.getPassword())
                .profile(providerUser.getProfile())
                .phone(providerUser.getPhone())
                .name(providerUser.getName())
                .verify(true).build();
            member.addMemberRole(MemberRole.USER);
            member.addAuthoritiesSet(providerUser.getAuthorities());

            memberRepository.save(member);
        return member;
    }
}
