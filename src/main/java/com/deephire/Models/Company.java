package com.deephire.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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


    private String description;


    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] logo;


    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] backgroundImage;



    @OneToOne
    @JoinColumn(name = "admin_id")
    @JsonBackReference("admin-company")
    private AdminCompany admin;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @JsonManagedReference("company-rh")
    private List<RHCompany> rhUsers;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @JsonManagedReference("company-jobposting")
    private List<JobPosting> jobPostings;
}