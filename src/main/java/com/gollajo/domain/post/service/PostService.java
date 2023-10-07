package com.gollajo.domain.post.service;

import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.member.repository.MemberRepository;
import com.gollajo.domain.post.dto.PostCreateRequest;
import com.gollajo.domain.post.entity.Post;
import com.gollajo.domain.post.entity.PostBody;
import com.gollajo.domain.post.entity.StringOption;
import com.gollajo.domain.post.entity.enums.PostState;
import com.gollajo.domain.post.entity.enums.PostType;
import com.gollajo.domain.post.repository.PostRepository;
import com.gollajo.domain.s3.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final AmazonS3Service amazonS3Service;

    public Long createStringPost(PostCreateRequest request, Member member){
        PostBody createdPostBody = PostBody.builder()
                .postType(PostType.STRING_OPTION)
                .title(request.title())
                .content(request.content())
                .maxVotes(request.maxVotes())
                .pointPerVote(request.pointPerVote())
                .expirationDate(request.expirationDate())
                .build();
        Post post = Post.builder()
                .postBody(createdPostBody)
                .member(member)
                .postState(PostState.STATE_GENERATING)
                .build();
        post.mapPostStringOption(post,request.optionContent());

        member.plusNumOfVoting();
        memberRepository.save(member);
        postRepository.save(post);
        return post.getId();

    }
}
