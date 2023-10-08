package com.gollajo.domain.account.service;

import com.gollajo.domain.account.entity.Account;
import com.gollajo.domain.account.entity.AccountBody;
import com.gollajo.domain.account.entity.enums.AccountState;
import com.gollajo.domain.account.entity.enums.AccountType;
import com.gollajo.domain.account.repository.AccountRepository;
import com.gollajo.domain.exception.handler.AccountExceptionHandler;
import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.member.repository.MemberRepository;
import com.gollajo.domain.exception.CustomException;
import com.gollajo.domain.exception.ErrorCode;
import com.gollajo.domain.post.entity.Post;
import com.gollajo.domain.post.entity.PostBody;
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

    private final AccountExceptionHandler accountExceptionHandler;

    public List<Account> showMyAccount() {
        //TODO: 실제론 jwt토큰으로 memberId를 받아옴, 추후 삭제 필요
        Long memberId = 1L;
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.NO_MEMBER_BY_MEMBER_ID));
        List<Account> accountList = accountRepository.findByTargetMember(member);
        return accountList;
    }

    // 투표글 생성시 거래내역 만들고 저장하는 함수
    public Account saveCreatePostAccount(Member member, Post post, String memo) {

        int sumAmount = post.getPostBody().getPointPerVote()
                * post.getPostBody().getMaxVotes();

        AccountBody accountBody = AccountBody.builder()
                .amount(sumAmount)
                .memo(memo)
                .accountType(AccountType.WITHDRAW)
                .accountState(AccountState.HOLDING)
                .build();

        Account account = Account.builder()
                .accountBody(accountBody)
                .targetMember(member)
                .targetPost(post)
                .build();

        accountRepository.save(account);
        return account;
    }

    //투표글 취소시 거래내역을 취소로 변경 후 저장하는 함수
    public Account saveCancelPostAccount(Member member,Post post){

        Account account = accountRepository.findByTargetMemberAndTargetPost(member, post)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_ACCOuNT_HISTORY));

        accountExceptionHandler.cancelAccountException(account);

        account.getAccountBody().setAccountStateToCancel();
        accountRepository.save(account);

        return account;
    }

    //투표시 투표를 통한 포인트 거래내역을 기록 후 저장하는 함수
    public Account saveVoteAccount(Member member,Post post){
        AccountBody accountBody = AccountBody.builder()
                .amount(post.getPostBody().getPointPerVote())
                .memo("Get voting point")
                .accountState(AccountState.COMPLETE)
                .accountType(AccountType.DEPOSIT)
                .build();

        Account account = Account.builder()
                .accountBody(accountBody)
                .targetMember(member)
                .targetPost(post)
                .build();

        accountRepository.save(account);
        return account;
    }

    public Account savePaymentAccount(Member member,int amount){
        AccountBody accountBody = AccountBody.builder()
                .amount(amount)
                .memo("Payment point")
                .accountState(AccountState.COMPLETE)
                .accountType(AccountType.DEPOSIT)
                .build();

        Account account = Account.builder()
                .accountBody(accountBody)
                .targetMember(member)
                .build();

        accountRepository.save(account);
        return account;
    }

}
