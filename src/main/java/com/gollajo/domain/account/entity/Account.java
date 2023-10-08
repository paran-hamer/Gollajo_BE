package com.gollajo.domain.account.entity;

import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_member")
    private Member targetMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_post")
    private Post targetPost;

    @Embedded
    private AccountBody accountBody;


    @Builder
    private Account(
            final Member targetMember,
            final Post targetPost,
            final AccountBody accountBody
    ) {
        this.targetMember = targetMember;
        this.targetPost = targetPost;
        this.accountBody = accountBody;
    }
}
