package com.findme.postcomment.model;

import com.findme.base.model.BaseEntity;
import com.findme.post.model.PostEntity;
import com.findme.profile.model.ProfileEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_comments")
@Getter
@NoArgsConstructor
public class PostCommentEntity extends BaseEntity {

    @NotNull
    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private ProfileEntity profile;

    // Constructors
    public PostCommentEntity(String comment, PostEntity post, ProfileEntity profile) {
        this.comment = comment;
        this.post = post;
        this.profile = profile;
    }

}
