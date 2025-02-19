package com.deephire.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class AdminCompany extends User {

    @OneToOne(mappedBy = "admin", cascade = CascadeType.ALL)
    private Company company;

    public void manageRHUser(RHCompany rhUser) {
        // Business logic
    }




}