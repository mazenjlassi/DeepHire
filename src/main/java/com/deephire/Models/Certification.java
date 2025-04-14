package com.deephire.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String issuer;
    private Date issueDate;
    private Date expirationDate;


    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
}