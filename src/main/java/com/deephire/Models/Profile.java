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
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String headline;
    private String summary;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<Experience> experiences;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<Education> education;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<Skill> skills;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<Certification> certifications;
}