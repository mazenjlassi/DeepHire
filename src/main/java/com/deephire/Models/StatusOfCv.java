package com.deephire.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference("user-status")
    private User user;

    @ManyToOne
    @JsonBackReference("jobposting-status")
    private JobPosting jobPosting;
}