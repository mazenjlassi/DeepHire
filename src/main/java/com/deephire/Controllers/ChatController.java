package com.deephire.Controllers;

import com.deephire.Dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload MessageDTO message, Principal principal) {
        try {
            System.out.println("[ChatController] Received message: " + message);
            System.out.println("[ChatController] Principal: " + principal.getName());

            if (message.getReceiverUsername() == null || message.getContent() == null) {
                throw new IllegalArgumentException("Receiver or content is null");
            }

            messagingTemplate.convertAndSendToUser(
                    message.getReceiverUsername(),
                    "/queue/messages",
                    message
            );

            System.out.println("[ChatController] Message forwarded to: " + message.getReceiverUsername());
        } catch (Exception e) {
            System.err.println("[ChatController] Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }






}
