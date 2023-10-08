package com.gollajo.domain.post.dto.response;

import lombok.Builder;

@Builder
public record PostInfoImageOption(
        Long id,
        String content,
        String imgUrl
)implements PostInfoOption {
}
