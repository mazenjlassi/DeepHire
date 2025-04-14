package com.deephire.Models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;
    private String requirements;
    private String location;
    private String datePosted;

    @ManyToOne
    private Company company;

    @OneToMany(mappedBy = "jobPosting", cascade = CascadeType.ALL)
    private List<StatusOfCv> statusOfCvs;
}