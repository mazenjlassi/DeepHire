package com.deephire.Models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class AdminCompany extends User {

    @OneToOne(mappedBy = "admin", cascade = CascadeType.ALL)
    private Company company;

    public void manageRHUser(RHCompany rhUser) {
        // Business logic
    }




}