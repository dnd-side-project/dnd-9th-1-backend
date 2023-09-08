package com.backend.member.application;

import com.backend.global.common.code.ErrorCode;
import com.backend.global.exception.BusinessException;
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

    public Boolean findMemberOrRegister(Provider provider, String uid) {
        Optional<Member> member =  memberRepository.findByUid(uid);
        if(member.isPresent()) // 기등록된 회원인 경우, 다시 저장하지 않는다.
            return false;
        memberRepository.save(Member.from(provider, uid));
        return true;
    }

    public void withdraw(String uid) {
        Member member = memberRepository.getByUid(uid);
        member.withdraw();
    }
}
