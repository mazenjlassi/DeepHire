package org.example.deephire.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class RHCompany extends User {

    @ManyToOne
    private Company company;


}