package com.gollajo.domain.member.dto;

import com.gollajo.domain.member.entity.enums.Gender;
import com.gollajo.domain.member.entity.enums.Grade;
import com.gollajo.domain.member.entity.enums.SocialType;
import lombok.Builder;

@Builder
public record RequestMypageDto(
        String email,
        String nickname,
        SocialType socialType,
        int point,
        Grade grade,
        int numOfVoting,
        Gender gender,
        int age,
        int accumulatedPoints
) {
}
