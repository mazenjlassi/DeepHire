package com.deephire.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
public class MessageDTO {
    private String content;
    private String senderUsername; // Must match Angular
    private String receiverUsername; // Must match Angular
    private Date timestamp;
    private String firstName;
    private String lastName;

    public MessageDTO(String content, Date timestamp, String senderUsername, String receiverUsername,String firstName, String lastName) {

        this.content = content;
        this.timestamp = timestamp;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.firstName = firstName;
        this.lastName = lastName;

    }

    // Getters and setters (required for JSON parsing)
}