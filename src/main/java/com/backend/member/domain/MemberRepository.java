package com.backend.member.domain;

import com.backend.global.common.code.ErrorCode;
import com.backend.global.exception.BusinessException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
     Optional<Member> findByUid(String uid);

     default Member getByUid(String uid){
          return findByUid(uid).orElseThrow(() -> {
               throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
          });
     }
}
