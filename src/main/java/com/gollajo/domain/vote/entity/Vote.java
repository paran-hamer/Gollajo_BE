package com.gollajo.domain.vote.entity;

import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.post.entity.ImageOption;
import com.gollajo.domain.post.entity.Post;
import com.gollajo.domain.post.entity.TextOption;
import com.gollajo.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Vote extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "string_option_id")
    private TextOption textOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_option_id")
    private ImageOption imageOption;

    @Builder
    private Vote(
            final Post post,
            final Member member,
            final TextOption textOption,
            final ImageOption imageOption
    ) {
        this.post = post;
        this.member = member;
        this.textOption = textOption;
        this.imageOption = imageOption;
    }
}
