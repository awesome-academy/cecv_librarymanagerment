package com.sun.librarymanagement.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "publishers")
@Getter
@Setter
@NoArgsConstructor
public class PublisherEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    public PublisherEntity(String name) {
        this.name = name;
    }
}
