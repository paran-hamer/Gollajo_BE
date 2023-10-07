package com.gollajo.global.exception.dto;

import lombok.Builder;

@Builder
public record ErrorCodeResponse(
        int customCode,
        String message
) {
}
