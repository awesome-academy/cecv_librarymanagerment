package com.sun.librarymanagement.domain.repository;

import com.sun.librarymanagement.domain.entity.FollowEntity;
import com.sun.librarymanagement.domain.model.FollowInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<FollowEntity, FollowInfo> {
}
