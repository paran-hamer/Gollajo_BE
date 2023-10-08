package com.gollajo.domain.post.repository;

import com.gollajo.domain.post.entity.TextOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextOptionRepository extends JpaRepository<TextOption, Long> {
}
