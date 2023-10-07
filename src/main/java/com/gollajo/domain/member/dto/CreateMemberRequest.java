package com.gollajo.domain.member.dto;

import com.gollajo.domain.member.entity.enums.Grade;
import com.gollajo.domain.member.entity.enums.SocialType;
import lombok.Builder;

@Builder
public record CreateMemberRequest(
        String email,
        String nickname,
        SocialType socialType,
        String socialId
) {
}
