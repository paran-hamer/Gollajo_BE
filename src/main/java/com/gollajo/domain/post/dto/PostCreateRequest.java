package com.gollajo.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostCreateRequest(
        String title,
        String content,
        @JsonProperty("max_votes")
        int maxVotes,
        @JsonProperty("point_per_vote")
        int pointPerVote,
        @JsonProperty("expiration_date")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime expirationDate,
        @JsonProperty("option_content")
        List<String> optionContent
) {
}
