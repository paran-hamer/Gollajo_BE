package com.gollajo.domain.exception.handler;

import com.gollajo.domain.post.repository.ImageOptionRepository;
import com.gollajo.domain.post.repository.PostRepository;
import com.gollajo.domain.post.repository.TextOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class VoteExceptionHandler {

    private final PostRepository postRepository;
    private final ImageOptionRepository imageOptionRepository;
    private final TextOptionRepository textOptionRepository;

    public void voteException(){

    }
}
