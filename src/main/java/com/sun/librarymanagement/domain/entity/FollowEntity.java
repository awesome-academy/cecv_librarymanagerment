package com.sun.librarymanagement.domain.entity;

import com.sun.librarymanagement.domain.model.FollowInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "follows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowEntity {

    @EmbeddedId
    private FollowInfo id;

    @ManyToOne
    @MapsId("followerId")
    @JoinColumn(name = "follower_id")
    private UserEntity follower;
}
