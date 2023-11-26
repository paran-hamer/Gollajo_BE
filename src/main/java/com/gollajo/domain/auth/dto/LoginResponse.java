package com.gollajo.domain.auth.dto;

import lombok.Builder;

@Builder
public record LoginResponse(
        String message
) {
}
