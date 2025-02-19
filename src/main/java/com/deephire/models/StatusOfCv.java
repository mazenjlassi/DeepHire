package com.deephire.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StatusOfCv {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String state;

    @ManyToOne
    private User User;

    @ManyToOne
    private JobPosting jobPosting;
}