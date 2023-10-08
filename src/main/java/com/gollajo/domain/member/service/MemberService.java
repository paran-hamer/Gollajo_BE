package com.gollajo.domain.member.service;

import com.gollajo.domain.account.entity.Account;
import com.gollajo.domain.account.service.AccountService;
import com.gollajo.domain.member.dto.CreateMemberRequest;
import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.member.entity.enums.Grade;
import com.gollajo.domain.exception.handler.MemberExceptionHandler;
import com.gollajo.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberExceptionHandler memberExceptionHandler;

    private final AccountService accountService;

    public String signUp(CreateMemberRequest request){

        memberExceptionHandler.signUpException(request);

        Member member = Member.builder()
                .email(request.email())
                .nickname(request.nickname())
                .socialType(request.socialType())
                .socialId(request.socialId())
                .point(0)
                .grade(Grade.LV1)
                .numOfVoting(0)
                .build();

        memberRepository.save(member);

        return "회원가입 성공";
    }

    public Member findById(Long memberId){

        memberExceptionHandler.findByIdException(memberId);

        return memberRepository.findById(memberId).get();
    }

    //내 정보를 업데이트 한다.
    public Member update(CreateMemberRequest request){

        memberExceptionHandler.signUpException(request);

        Member member = Member.builder()
                .email(request.email())
                .nickname(request.nickname())
                .socialType(request.socialType())
                .socialId(request.socialId())
                .build();

        memberRepository.save(member);

        return member;
    }


    //투표글 생성시 포인트 차감하는 함수
    public Member saveCreatePostMember(Member member,int minusAmount){

        member.minusPoint(minusAmount);

        memberRepository.save(member);
        return member;
    }

    //투표글 취소시 포인트 반환해주는 함수
    public Member saveCancelPostMember(Member member,int plusAmount){

        member.plusPoint(plusAmount);

        memberRepository.save(member);
        return member;
    }

    //투표시 포인트를 획득하는 함수
    public Member saveVotePostMember(Member member,int plusAmount){

        member.plusNumOfVoting();
        member.plusPoint(plusAmount);

        memberRepository.save(member);
        return member;
    }

    //포인트 충전시 포인트 충전해주는 함수
    public int savePaymentMember(Member adminMember,Member targetMember,int amount){

        //TODO : adminMember가 운영자권한인지 확인해서 아니면 예외처리하기

        targetMember.plusPoint(amount);
        memberRepository.save(targetMember);

        //거래내역 작성
        accountService.savePaymentAccount(targetMember, amount);

        return targetMember.getPoint();
    }

}
