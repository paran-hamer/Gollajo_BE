package com.gollajo.domain.post.dto.response;

import com.gollajo.domain.post.dto.response.PostInfoOption;
import com.gollajo.domain.post.entity.enums.PostState;
import com.gollajo.domain.post.entity.enums.PostType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostInfoResponse(
        Long postId,
        String title,
        String content,
        PostType postType,
        PostState postState,
        int currentPostCount,
        int maxPostCount,
        int pointPerVote,
        LocalDateTime createdAt,
        LocalDateTime expirationDate,
        List<PostInfoOption> options
) {
    @Override
    public String toString() {
        return "PostInfoResponse{" +
                "expirationDate=" + expirationDate +
                '}';
    }
}
