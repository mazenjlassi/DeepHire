package com.deephire.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
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
    private Date datePosted;

    @ManyToOne
    @JsonBackReference("company-jobposting")
    private Company company;

    @OneToMany(mappedBy = "jobPosting", cascade = CascadeType.ALL)
    @JsonManagedReference("jobposting-status")
    private List<StatusOfCv> statusOfCvs;
}