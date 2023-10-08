package com.gollajo.domain.account.repository;

import com.gollajo.domain.account.entity.Account;
import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    List<Account> findByTargetMember(Member member);

    Optional<Account> findByTargetMemberAndTargetPost(Member member, Post post);

}
