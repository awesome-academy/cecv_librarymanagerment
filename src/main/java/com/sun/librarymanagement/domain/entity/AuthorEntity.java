package com.sun.librarymanagement.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "authors")
@Getter
@Setter
@NoArgsConstructor
public class AuthorEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    private String bio;

    private LocalDate dateOfBirth;

    public AuthorEntity(String name, String bio, LocalDate dateOfBirth) {
        this.name = name;
        this.bio = bio;
        this.dateOfBirth = dateOfBirth;
    }
}
