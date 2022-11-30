package io.web.chewing.repository;

import io.web.chewing.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Object> {
}
