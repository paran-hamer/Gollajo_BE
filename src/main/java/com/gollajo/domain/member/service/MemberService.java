package com.gollajo.domain.member.service;

import com.gollajo.domain.member.dto.CreateMemberRequest;
import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.member.entity.enums.Grade;
import com.gollajo.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public String signUp(CreateMemberRequest createMemberRequest){
        Member member = Member.builder()
                .email(createMemberRequest.email())
                .nickname(createMemberRequest.nickname())
                .socialType(createMemberRequest.socialType())
                .socialId(createMemberRequest.socialId())
                .point(0)
                .grade(Grade.LV1)
                .numOfVoting(0)
                .build();

        memberRepository.save(member);
        return "Success";
    }
}
