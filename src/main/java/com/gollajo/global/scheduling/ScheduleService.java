package com.gollajo.global.scheduling;

import com.gollajo.domain.account.entity.Account;
import com.gollajo.domain.account.entity.enums.AccountState;
import com.gollajo.domain.account.entity.enums.AccountType;
import com.gollajo.domain.account.repository.AccountRepository;
import com.gollajo.domain.post.entity.Post;
import com.gollajo.domain.post.entity.enums.PostState;
import com.gollajo.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class ScheduleService {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;

    @Scheduled(fixedRate = 60000)
    @Modifying
    @Transactional(readOnly = true)
    public void updatePostState(){

        List<Post> posts = postRepository.findAll();
        LocalDateTime currentTime = LocalDateTime.now();

        for (Post post : posts) {

            LocalDateTime createdAt = post.getCreatedAt();
            LocalDateTime expirationAt = post.getPostBody().getExpirationDate();
            LocalDateTime expirationTime = createdAt.plusMinutes(2);

            // 현재시간과 생성된 시간을 비교하여 투표글의 상태를 업데이트
            if (currentTime.isAfter(expirationTime)) {
                log.info("VoteStatus UPDATE: GENERATING -> CONTINUE");
                post.setPostState(PostState.STATE_PROCEEDING);
                postRepository.save(post);
                List<Account> allAccount = accountRepository.findByTargetMember(post.getMember());
                // 해당하는 거래내역 갱신
                for (Account account : allAccount) {
                    if (account.getAccountBody().getAccountType() == AccountType.WITHDRAW &&
                            account.getAccountBody().getAccountState() == AccountState.HOLDING) {
                        account.getAccountBody().setAccountStateToComplete();
                        accountRepository.save(account);
                    }
                }
            }
            // 현재시간과 투표글 마감날짜를 비교하여 투표글 상태를 업데이트
            if (currentTime.isAfter(expirationAt)) {
                if (post.getPostState()==PostState.STATE_COMPLETE){
                    post.setPostState(PostState.STATE_EXPIRED_AND_COMPLETE);
                }else{
                    post.setPostState(PostState.STATE_EXPIRED_AND_NO_COMPLETE);
                }
                postRepository.save(post);
            }
        }
    }

}
