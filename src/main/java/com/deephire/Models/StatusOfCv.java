package com.deephire.Models;

import com.deephire.Enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StatusOfCv {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status state=Status.PENDING;

    @ManyToOne
    @JsonBackReference("user-status")
    private User user;

    @ManyToOne
    @JsonBackReference("jobposting-status")
    private JobPosting jobPosting;


    public Status getStatus() {
        return state;
    }
}