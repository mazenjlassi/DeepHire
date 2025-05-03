package com.deephire.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KpiDto {
    private String title;
    private long value;
}
