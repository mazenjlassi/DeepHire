package org.example.deephire.models;

import jakarta.persistence.*;
import lombok.*;

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