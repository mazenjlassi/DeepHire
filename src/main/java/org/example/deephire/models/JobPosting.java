package org.example.deephire.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

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