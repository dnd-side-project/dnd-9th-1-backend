package com.backend.member.application;

import com.backend.member.domain.Member;
import com.backend.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findMemberOrRegister(Member uncheckedMember) {
        Optional<Member> member =  memberRepository.findBySocialId(uncheckedMember.getSocialId());
        return member.orElseGet(() -> memberRepository.save(uncheckedMember));
    }
}
