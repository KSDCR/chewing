package io.web.chewing.member;

import io.web.chewing.Entity.Member;
import io.web.chewing.repository.MemberCustomRepositoryImpl;
import io.web.chewing.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@RequiredArgsConstructor
@Slf4j
public class MemberTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberCustomRepositoryImpl memberCustomRepository;

/*    @After
    public void tearDown() throws Exception {
        memberRepository.deleteAllInBatch();
    }*/

    @Test
    public void querydsl_memberTest() {
        //given
        String email = "crow@crow.com";
        String name = "kim";
        String nickname = "crow";
        String password = "1234";
        String provider = "test";
        memberRepository.save(new Member(email, passwordEncoder.encode(password), nickname, provider));

        //when
        List<Member> result = memberCustomRepository.findByEmail(email);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProvider()).isEqualTo(provider);

    }
    @Test
    @Transactional
   public void findByEmail(){
        // given
        String email = "oneso123456789@gmail.com";

        //when
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member = optionalMember.orElseThrow();

        log.info("냥냥펀치"+String.valueOf(member.getAuthoritiesSet()));

        //than
    }
}
