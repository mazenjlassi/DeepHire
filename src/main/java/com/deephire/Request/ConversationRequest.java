package com.deephire.Request;

import lombok.Data;

import java.util.Date;


@Data
public class ConversationRequest {

    private String content;
    private String senderUsername;
    private String receiverUsername;
    private Date timestamp;

    public ConversationRequest(String content, Date timestamp, String senderUsername, String receiverUsername) {

        this.content = content;
        this.timestamp = timestamp;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;

    }

    // Getters and setters (required for JSON parsing)
}