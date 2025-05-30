package com.sun.librarymanagement.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @JoinColumn(name = "publisher_id", nullable = false)
    @ManyToOne
    private PublisherEntity publisher;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "available_quantity", nullable = false)
    private int availableQuantity;

    @JoinColumn(name = "author_id", nullable = false)
    @ManyToOne
    private AuthorEntity author;

    @JoinTable(
        name = "book_category",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @ManyToMany
    private Set<CategoryEntity> categories;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @JoinTable(
        name = "favorites",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @ManyToMany
    private Set<UserEntity> favorites;
}
