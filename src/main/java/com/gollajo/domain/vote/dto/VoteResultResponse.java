package com.gollajo.domain.vote.dto;

import com.gollajo.domain.post.entity.enums.PostType;
import lombok.Builder;

@Builder
public record VoteResultResponse(
        long optionId,
        String optionContent,
        int voteCount,
        PostType postType
) {
}
