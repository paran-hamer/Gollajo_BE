package com.gollajo.domain.post.repository;

import com.gollajo.domain.post.entity.ImageOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageOptionRepository extends JpaRepository<ImageOption, Long> {
}
