package com.gollajo.domain.account.exception;

import com.gollajo.domain.account.entity.Account;
import com.gollajo.domain.account.entity.enums.AccountState;
import com.gollajo.domain.account.entity.enums.AccountType;
import com.gollajo.domain.account.repository.AccountRepository;
import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.post.entity.Post;
import com.gollajo.global.exception.CustomException;
import com.gollajo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AccountExceptionHandler {

    private final AccountRepository accountRepository;

    public void cancelAccountException(Account account){

        // 포인트 차감(투표글 생성)된 기록인지 확인
        if(account.getAccountBody().getAccountType()!= AccountType.WITHDRAW){
            throw new CustomException(ErrorCode.NO_WITHDRAW_HISTORY);
        }

        // 취소가능상태인지 확인
        if(account.getAccountBody().getAccountState()!= AccountState.HOLDING){
            throw new CustomException(ErrorCode.NO_WAITING_ACCOUNT_STATUS);
        }

    }
}
