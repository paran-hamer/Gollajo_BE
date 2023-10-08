package com.gollajo.domain.exception.dto;

import lombok.Builder;

@Builder
public record ErrorCodeResponse(
        int customCode,
        String message
) {
}
