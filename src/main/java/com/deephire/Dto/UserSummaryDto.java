package com.deephire.Dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String backgroundPicture;
}