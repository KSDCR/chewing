package io.web.chewing.config.security.service;

import io.web.chewing.Entity.Member;
import io.web.chewing.Entity.MemberRole;
import io.web.chewing.config.security.dto.AuthMemberDTO;
import io.web.chewing.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class OAuthUserDetailsService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)  {

        log.info("userRequest.....");
        log.info(userRequest);

        log.info("oauth2 user .......................");

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        log.info("NAME: " + clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oAuth2User:"+oAuth2User);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        paramMap.forEach((s, o) -> {
            log.info("--------------------");
            log.info(s + ":" + o);
        });


        String email = getEmail(clientName, paramMap);

        log.info("===============================");
        log.info(email);
        log.info("===============================");
        Member member = saveSocialMember(email,paramMap);


        AuthMemberDTO authMemberDTO = new AuthMemberDTO(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
                member.getNickname(),
                member.getProvider(),
                member.getName(),
                member.getDelete_yn(),
                member.isVerify(),
                List.of(new SimpleGrantedAuthority("USER")));
        authMemberDTO.setAttr(paramMap);
        log.info("Member:" + member);
        log.info("authMemberDTO:"+authMemberDTO);

        return authMemberDTO;
    }


    private String getEmail(String key, Map<String, Object> paramMap) {

        log.info("getEmail----------------------------");
        paramMap.forEach((s, o) -> log.info("paramMap: " + s + ":" + o));

        Object value = paramMap.get(injectKey(key));
        log.info(value.getClass());
        log.info("value="+value);
        String email;
        LinkedHashMap<String,Object> accountMap;

        if(value.getClass() != String.class ){
           accountMap = (LinkedHashMap<String, Object>) value;
           accountMap.forEach((o, o2) -> log.info(o + ":" + o2));

            email = (String) accountMap.get("email");

            log.info("email...." + email);
        }else {
            email = (String) paramMap.get("email");
        }

        log.info("이메일은?"+email);





        return email;
    }


    private String injectKey(String clientName) {

        return switch (clientName) {
            case "kakao" -> "kakao_account";
            case "naver" -> "response";
            default -> "email";
        };
    }

    @Transactional
    Member saveSocialMember(String email, Map<String, Object> paramMap) {
        // 기존에 동일한 이메일로 가입한 회원은 그대로 조회만
        return memberRepository.findByEmail(email)
                .orElseGet(() -> getNewMember(email,paramMap));
    }

    private Member getNewMember(String email, Map<String, Object> paramMap) {
        // 없다면 회원 추가 소셜 아이디라서 패스워드를 사용할일이 없음 그러니 임의로 1111지정 이메일은 연동한 이메일
        Member newMember = Member.builder()
                .email(email)
                .password(passwordEncoder.encode("1111"))
                .nickname(email)
                .name(email)
                .provider("test")
                .phone("test")
                .gender('t')
                .delete_yn('0')
                .build();

        newMember.addMemberRole(MemberRole.USER);
        memberRepository.save(newMember);

        return newMember;
    }
}
