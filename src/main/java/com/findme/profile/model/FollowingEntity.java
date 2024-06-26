package com.findme.profile.model;

import com.findme.base.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "following")
@Getter
@NoArgsConstructor
public class FollowingEntity extends BaseEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private ProfileEntity follower;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "following_id")
    private ProfileEntity following;

    public FollowingEntity(ProfileEntity follower, ProfileEntity following) {
        this.follower = follower;
        this.following = following;
    }

}
