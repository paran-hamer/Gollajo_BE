package com.gollajo.domain.post.repository;

import com.gollajo.domain.post.entity.ImageOption;
import com.gollajo.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageOptionRepository extends JpaRepository<ImageOption, Long> {

    List<ImageOption> findAllByPost(Post post);

}
