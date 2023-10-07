package com.gollajo.domain.post.dto;

import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.post.entity.enums.PostType;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostCreateRequest(
        Member member,
        String title,
        String content,
        int maxVotes,
        int pointPerVote,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime expirationDate,
        List<String> optionContent
) {
}
