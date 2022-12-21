package io.web.chewing.config.security.service;

import io.web.chewing.config.security.dto.AuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {

    private final MemberRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("----------------------------");
        log.info("AuthUserDetailsService loadUserByUsername" + username);

        Member member = checkMember(username);
        log.info("------------------------------------------");
        log.info(member);
        log.info("냥냥펀치확인");

        AuthMemberDTO dto = new AuthMemberDTO(
                member.getId(), member.getEmail(),
                member.getPassword(),
                member.getNickname(),
                member.getProvider(),
                member.getName(),
                member.getDelete_yn(),
                member.isVerify(),
                member.getRoleSet().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toList())
        );

        log.info("AuthMemberDTO");
        log.info(dto);

        return dto;
    }

    private Member checkMember(String username) {

        return repository.getWithRoles(username).orElseThrow(() -> new UsernameNotFoundException("아이디가 없습니다."));
    }


}
