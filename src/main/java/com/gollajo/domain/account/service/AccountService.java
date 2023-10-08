package com.gollajo.domain.account.service;

import com.gollajo.domain.account.entity.Account;
import com.gollajo.domain.account.repository.AccountRepository;
import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.member.repository.MemberRepository;
import com.gollajo.global.exception.CustomException;
import com.gollajo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountService {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;

    public List<Account> showMyAccount(){
        //TODO: 실제론 jwt토큰으로 memberId를 받아옴, 추후 삭제 필요
        Long memberId = 1L;
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.NO_MEMBER_BY_MEMBER_ID));
        List<Account> accountList = accountRepository.findByTargetMember(member);
        return accountList;
    }
}
