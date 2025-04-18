package com.sun.librarymanagement.domain.repository;

import com.sun.librarymanagement.domain.entity.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<PublisherEntity, Long> {

    boolean existsByName(String name);
}
