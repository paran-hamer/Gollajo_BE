package com.gollajo.domain.vote.service;

import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.post.repository.PostRepository;
import com.gollajo.domain.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;

    public void vote(Member member, Long postId, Long optionId) {

    }

}
