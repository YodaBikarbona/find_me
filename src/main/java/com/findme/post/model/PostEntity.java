package com.findme.post.model;

import com.findme.base.model.BaseEntity;
import com.findme.postcomment.model.PostCommentEntity;
import com.findme.postimage.model.PostImageEntity;
import com.findme.profile.model.ProfileEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor
public class PostEntity extends BaseEntity {

    @NotNull
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "views")
    @Min(0)
    private int views;

    @NotNull
    @Column(name = "longitude")
    @Min(-180)
    @Max(180)
    private float longitude;

    @NotNull
    @Column(name = "latitude")
    @Min(-90)
    @Max(90)
    private float latitude;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private ProfileEntity profile;

    @OneToMany(mappedBy = "post")
    private List<PostImageEntity> postImage;

    @OneToMany(mappedBy = "post")
    private List<PostCommentEntity> postComment;

    // Constructors
    public PostEntity(String description, float longitude, float latitude, ProfileEntity profile) {
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.profile = profile;
        this.views = 0;
    }

    public void addPostImage(PostImageEntity postImage) {
        if (this.postImage == null) {
            this.postImage = new ArrayList<>();
        }
        this.postImage.add(postImage);
    }

    public void increaseViews() {
        this.views++;
    }

}
