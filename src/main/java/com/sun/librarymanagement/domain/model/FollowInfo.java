package com.sun.librarymanagement.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowInfo {

    @JoinColumn(name = "follower_id")
    private Long followerId;

    @JoinColumn(name = "followee_id")
    private Long followeeId;

    @Enumerated(EnumType.STRING)
    private FollowType type;
}
