package com.deephire.Controllers;

import com.deephire.Dto.MessageDTO;
import com.deephire.Models.Message;
import com.deephire.Models.User;
import com.deephire.Repositories.MessageRepository;
import com.deephire.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Date;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @MessageMapping("/chat.send")
    @Transactional
    public void sendMessage(@Payload MessageDTO messageDTO, Principal principal) {
        // Get sender and receiver from database
        User sender = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findByUsername(messageDTO.getReceiverUsername())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Create and save the message
        Message message = new Message();
        message.setContent(messageDTO.getContent());
        message.setTimestamp(new Date());
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setRead(false);

        messageRepository.save(message);

        // Send the message via WebSocket
        messagingTemplate.convertAndSendToUser(
                messageDTO.getReceiverUsername(),
                "/queue/messages",
                messageDTO
        );

        // Also send a copy to sender (optional)
        messagingTemplate.convertAndSendToUser(
                principal.getName(),
                "/queue/messages",
                messageDTO
        );
    }
}