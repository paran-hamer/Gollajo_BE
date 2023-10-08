package com.gollajo.domain.account.entity;

import com.gollajo.domain.account.entity.enums.AccountState;
import com.gollajo.domain.account.entity.enums.AccountType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class AccountBody {

    @Column
    private int amount;

    @Column
    private String memo;

    @Column(name="state")
    @Enumerated(EnumType.STRING)
    private AccountState accountState;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Builder
    private AccountBody(
            final int amount,
            final String memo,
            final AccountState accountState,
            final AccountType accountType
    ){
        this.amount = amount;
        this.memo = memo;
        this.accountState = accountState;
        this.accountType = accountType;
    }

    public void setAccountStateToComplete(){
        this.accountState = AccountState.COMPLETE;
    }
    public void setAccountStateToCancel(){
        this.accountState = AccountState.CANCEL;
    }



}
