package io.web.chewing.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.web.chewing.Entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.web.chewing.Entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl {
    private final JPAQueryFactory queryFactory;

        public List<Member> findByEmail(String email){
            return queryFactory.selectFrom(member)
                    .where(member.email.eq(email))
                    .fetch();
        }
}
