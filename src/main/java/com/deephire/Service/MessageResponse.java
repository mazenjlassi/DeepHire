package com.deephire.Service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
@Getter
@Setter

public class MessageResponse {

    private String message;
    public MessageResponse(String message) {this.message = message;}
}
