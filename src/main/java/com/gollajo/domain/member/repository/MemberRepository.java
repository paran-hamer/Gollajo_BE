package com.gollajo.domain.member.repository;

import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.member.entity.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findBySocialIdAndSocialType(String SocialId, SocialType socialType);

    Optional<Member> findByEmail(String email);

}
