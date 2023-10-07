package com.gollajo.domain.post.repository;

import com.gollajo.domain.post.entity.StringOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StringOptionRepository extends JpaRepository<StringOption, Long> {
}
