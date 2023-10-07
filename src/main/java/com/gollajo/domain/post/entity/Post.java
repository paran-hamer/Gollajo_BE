package com.gollajo.domain.post.entity;

import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.post.entity.enums.PostState;
import com.gollajo.domain.post.entity.enums.PostType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="author_id")
    private Member member;

    @Embedded
    private PostBody postBody;

    @Column(name = "post_state")
    @Enumerated(EnumType.STRING)
    private PostState postState;

    //TODO: cascade타입을 all로 두면 안될거같음. 변경 필요
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StringOption> stringOptions = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageOption> imageOptions = new ArrayList<>();

    @Builder
    private Post(
            final Member member,
            final PostBody postBody,
            final PostState postState
    ){
        this.member = member;
        this.postBody = postBody;
        this.postState = postState;
    }

    public void mapPostStringOption(Post post,List<String> optionContent){
        for(String content:optionContent){
            StringOption stringOption = StringOption.builder()
                    .post(post)
                    .stringContent(content)
                    .build();
            this.stringOptions.add(stringOption);
        }
    }

}
