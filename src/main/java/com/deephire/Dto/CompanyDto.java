package com.deephire.Dto;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode

public class CompanyDto {
    private Long id;

    private String name;
    private String industry;
    private String location;


    private String description;
    private String logo;

    private String backgroundImage;


}
