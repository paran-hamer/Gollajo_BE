package com.gollajo.global.scheduling;

import com.gollajo.domain.account.entity.Account;
import com.gollajo.domain.account.entity.enums.AccountState;
import com.gollajo.domain.account.entity.enums.AccountType;
import com.gollajo.domain.account.repository.AccountRepository;
import com.gollajo.domain.account.service.AccountService;
import com.gollajo.domain.post.entity.Post;
import com.gollajo.domain.post.entity.enums.PostState;
import com.gollajo.domain.post.repository.PostRepository;
import com.gollajo.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class ScheduleService {

    private final PostRepository postRepository;
    private final PostService postService;

    @Scheduled(fixedRate = 30000)
    @Modifying
    @Transactional
    public void updatePostState(){

        final List<Post> posts = postRepository.findAll();

        for (Post post : posts) {

            // 현재시간과 생성된 시간을 비교하여 투표글의 상태를 업데이트
            postService.updateProceed(post);

            // 현재시간과 투표글 마감날짜를 비교하여 투표글 상태를 업데이트
            postService.checkExpired(post);
        }
    }

}
