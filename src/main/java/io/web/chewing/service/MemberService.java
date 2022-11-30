package io.web.chewing.service;


import io.web.chewing.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;

}
