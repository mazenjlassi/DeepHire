package org.example.deephire.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

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
    private org.example.deephire.models.User User;

    @ManyToOne
    private JobPosting jobPosting;
}