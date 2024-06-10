package com.findme.postimage.model;

import com.findme.base.model.BaseEntity;
import com.findme.post.model.PostEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "post_images")
@Getter
@Setter
@NoArgsConstructor
public class PostImageEntity extends BaseEntity {

    @NotNull
    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    // Constructors
    public PostImageEntity(String url, PostEntity post) {
        this.url = url;
        this.post = post;
    }

}
