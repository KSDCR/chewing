package io.web.chewing.repository;

import io.web.chewing.Entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Object> {

    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.email = :email")
    Optional<Member> getWithRoles(String email);

}
