package com.sun.librarymanagement.domain.entity;

import com.sun.librarymanagement.domain.model.RateInfo;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RateEntity {

    @EmbeddedId
    private RateInfo id;

    private Integer rate;
}
