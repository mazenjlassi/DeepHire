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
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String headline;
    private String summary;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-profile")
    private User user;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    @JsonManagedReference("profile-experience")
    private List<Experience> experiences;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    @JsonManagedReference("profile-education")
    private List<Education> education;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    @JsonManagedReference("profile-skill")
    private List<Skill> skills;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    @JsonManagedReference("profile-certification")
    private List<Certification> certifications;
}