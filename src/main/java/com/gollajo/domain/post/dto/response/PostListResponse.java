package com.gollajo.domain.post.dto.response;

import com.gollajo.domain.post.entity.enums.PostType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostListResponse (
        Long postId,
        PostType postType,
        String title,
        String content,
        int pointPerVote,
        LocalDateTime expirationDate
){
}
