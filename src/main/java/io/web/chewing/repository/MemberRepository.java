package io.web.chewing.repository;

import io.web.chewing.Entity.Categories;
import io.web.chewing.Entity.Member;
import io.web.chewing.Entity.MemberRole;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface MemberRepository extends JpaRepository<Member,Object> {

    @EntityGraph(attributePaths = {"roleSet", "categoriesSet"})
    @Query("select m from Member m where m.email = :email")
    Optional<Member> getWithRoles(String email);

    @EntityGraph(attributePaths = {"roleSet","categoriesSet"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Member> findByEmail(String email);



}
