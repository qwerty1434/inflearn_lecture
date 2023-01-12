package com.example.jwttutorial.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authority")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Authority {
    @Id
    @Column(name="authority_name", length = 50)
    private String authorityName;
}
