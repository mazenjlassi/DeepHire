package org.example.deephire.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String industry;
    private String location;

    @OneToOne
    @JoinColumn(name = "admin_id")
    private AdminCompany admin;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<RHCompany> rhUsers;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<JobPosting> jobPostings;
}