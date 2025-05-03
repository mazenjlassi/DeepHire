package com.deephire.Dto;

import lombok.Data;

@Data
public class JobPostingUpdateWrapperDTO {
    private JobPostingRequestDTO original;
    private JobPostingRequestDTO updated;
}
