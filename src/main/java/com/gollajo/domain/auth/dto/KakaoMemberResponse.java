package com.gollajo.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoMemberResponse (
        @JsonProperty("id") Long id,
        @JsonProperty("kakao_account") KakaoAccount kakaoAccount
){
    public record KakaoAccount(
            @JsonProperty("email") String email,
            @JsonProperty("age_range") String ageRange,
            @JsonProperty("birthday") String birthday,
            @JsonProperty("gender") String gender,
            @JsonProperty("profile") Profile profile
    ){
        public record Profile(@JsonProperty("nickname") String nickname){}

    }
}
