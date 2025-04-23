package com.deephire.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MessageDTO {
    private String content;
    private String senderUsername; // Must match Angular
    private String receiverUsername; // Must match Angular
    private Date timestamp;

    // Getters and setters (required for JSON parsing)
}