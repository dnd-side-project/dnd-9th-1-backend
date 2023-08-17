package com.backend.member.application;

import com.backend.member.domain.Member;
import com.backend.member.domain.MemberRepository;
import com.backend.member.domain.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findMemberOrRegister(Provider provider, String uid) {
        Optional<Member> member =  memberRepository.findByUid(uid);
        return member.orElseGet(() -> memberRepository.save(Member.from(provider, uid)));
    }
}
