package com.gollajo.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Schema(name="게시글 생성 관련 데이터",description = "게시글 생성 요청에 관련한 데이터들입니다.")
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
