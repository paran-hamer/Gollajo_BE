package com.gollajo.domain.post.entity;

import com.gollajo.domain.post.entity.enums.PostType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class PostBody {

    @Column(name="post_type")
    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 100, nullable = false)
    private String content;

    @Column(name="max_votes",nullable = false)
    private int maxVotes;

    @Column(name="point_per_vote",nullable = false)
    private int pointPerVote;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Builder
    private PostBody(
            final PostType postType,
            final String title,
            final String content,
            final int maxVotes,
            final int pointPerVote,
            final LocalDateTime expirationDate
    ){
        this.postType = postType;
        this.title = title;
        this.content = content;
        this.maxVotes = maxVotes;
        this.pointPerVote = pointPerVote;
        this.expirationDate = expirationDate;
    }
}
