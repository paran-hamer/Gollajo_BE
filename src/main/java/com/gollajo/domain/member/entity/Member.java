package com.gollajo.domain.member.entity;

import com.gollajo.domain.member.entity.enums.Gender;
import com.gollajo.domain.member.entity.enums.Grade;
import com.gollajo.domain.member.entity.enums.SocialType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(length = 20,nullable = false)
    private String nickname;

    @Column
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column
    private String socialId;

    @Column(nullable = false)
    private int point;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(nullable = false)
    private int numOfVoting;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private int age;

}
