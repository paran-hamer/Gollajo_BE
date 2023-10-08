package com.gollajo.domain.post.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ImageOption implements Option{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_option_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post")
    private Post post;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name="image_content")
    private String imageContent;

    @Builder
    private ImageOption(Post post,String imageUrl,String imageContent){
        this.post = post;
        this.imageUrl = imageUrl;
        this.imageContent = imageContent;
    }


}
