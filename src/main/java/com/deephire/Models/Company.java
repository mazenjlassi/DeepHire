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