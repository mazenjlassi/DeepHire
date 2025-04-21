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
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String schoolName;
    private String degree;
    private String fieldOfStudy;
    private String startDate;
    private String endDate;

    @ManyToOne
    @JsonBackReference("profile-education")
    private Profile profile;
}