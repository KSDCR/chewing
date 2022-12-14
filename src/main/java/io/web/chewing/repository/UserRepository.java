package io.web.chewing.repository;

import io.web.chewing.Entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
public class UserRepository {

    private Map<String, Object> members = new HashMap<>();

    public Member findByEmail(String email) {
        if (members.containsKey(email)) {
            return (Member) members.get(email);
        }
        return null;
    }

    public void register(Member member) {
        if (members.containsKey(member.getEmail())) {
            return;
        }
        members.put(member.getEmail(), member);
        members.forEach((s, o) -> log.info("냥냥펀치" + s + ":" + o));
    }
}
