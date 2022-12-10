package io.web.chewing.service;

import io.web.chewing.Entity.Member;
import io.web.chewing.Entity.MemberRole;
import io.web.chewing.model.ProviderUser;
import io.web.chewing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Member register(ProviderUser providerUser){
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

            userRepository.register(member);
        return member;
    }
}
