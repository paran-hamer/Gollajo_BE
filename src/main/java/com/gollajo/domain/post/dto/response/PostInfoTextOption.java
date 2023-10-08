package com.gollajo.domain.post.dto.response;

import com.gollajo.domain.post.dto.response.PostInfoOption;
import lombok.Builder;

@Builder
public record PostInfoTextOption (
        Long id,
        String content
)implements PostInfoOption {
}
