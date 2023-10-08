package com.gollajo.domain.post.repository;

import com.gollajo.domain.post.entity.Post;
import com.gollajo.domain.post.entity.TextOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextOptionRepository extends JpaRepository<TextOption, Long> {

    List<TextOption> findAllByPost(Post post);

}
