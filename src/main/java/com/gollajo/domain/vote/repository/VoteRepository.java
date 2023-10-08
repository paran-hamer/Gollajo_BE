package com.gollajo.domain.vote.repository;

import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.post.entity.Post;
import com.gollajo.domain.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {

    boolean existsByMemberAndPost(Member member, Post post);

    int countByPost(Post post);

    List<Vote> findAllByPost(Post post);

}
