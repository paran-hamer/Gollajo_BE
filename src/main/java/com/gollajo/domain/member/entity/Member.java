package com.gollajo.domain.member.entity;

import com.gollajo.domain.auth.dto.KakaoMemberResponse;
import com.gollajo.domain.member.entity.enums.Gender;
import com.gollajo.domain.member.entity.enums.Grade;
import com.gollajo.domain.member.entity.enums.SocialType;
import com.gollajo.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //필수정보
    @Column(nullable = false)
    private String email;

    @Column(length = 20,nullable = false)
    private String nickname;

    //소셜로그인시
    @Column
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column
    private String socialId;

    //자동기입: 기본정보
    @Column(nullable = false)
    private int point;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(nullable = false)
    private int numOfVoting;

    //추가정보
    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private int age;
    @Builder
    private Member(
            final String email,
            final String nickname,
            final SocialType socialType,
            final String socialId,
            final int point,
            final Grade grade,
            final int numOfVoting,
            final Gender gender,
            final int age
                   ){
        this.email = email;
        this.nickname = nickname;
        this.socialType = socialType;
        this.socialId = socialId;
        this.point = point;
        this.grade = grade;
        this.numOfVoting = numOfVoting;
        this.gender = gender;
        this.age = age;

    }
    public void plusNumOfVoting(){
        this.numOfVoting = this.numOfVoting + 1;
    }

    public void plusPoint(int returnAmount){
        this.point += returnAmount;
    }

    public void minusPoint(int returnAmount){
        this.point -= returnAmount;
    }

    public static Member createKakaoMember(final KakaoMemberResponse response){

        final NicknameNumberGenerator nicknameNumberGenerator = new NicknameNumberGenerator();

        return Member.builder()
                .email(response.kakaoAccount().email())
                .nickname("익명의 사용자" + nicknameNumberGenerator.generate())
                .socialType(SocialType.KAKAO)
                .socialId(response.id().toString())
                .point(0)
                .grade(Grade.LV1)
                .numOfVoting(0)
                .gender(Gender.valueOf(response.kakaoAccount().gender().toUpperCase()))
                .build();
    }
}
