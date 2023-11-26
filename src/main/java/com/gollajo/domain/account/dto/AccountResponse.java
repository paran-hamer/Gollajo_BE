package com.gollajo.domain.account.dto;

import com.gollajo.domain.account.entity.enums.AccountState;
import com.gollajo.domain.account.entity.enums.AccountType;
import lombok.Builder;

@Builder
public record AccountResponse(AccountState accountState,
                              AccountType accountType,
                              String memo,
                              int amount,
                              long targetPost,
                              long targetMember,
                              long accountId) {
}
