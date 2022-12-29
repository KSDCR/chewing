package io.web.chewing.member;

import io.web.chewing.Entity.Member;
import io.web.chewing.repository.MemberCustomRepositoryImpl;
import io.web.chewing.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@RequiredArgsConstructor
public class MemberTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberCustomRepositoryImpl memberCustomRepository;

    @After
    public void tearDown() throws Exception {
        memberRepository.deleteAllInBatch();
    }

    @Test
    public void querydsl_memberTest() {
        //given
        String email = "crow@crow.com";
        String name = "kim";
        String nickname = "crow";
        String password = "1234";
        String provider = "test";
        memberRepository.save(new Member(email, passwordEncoder.encode(password), provider));

        //when
        List<Member> result = memberCustomRepository.findByEmail(email);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProvider()).isEqualTo(provider);

    }
}
