package com.deephire.Controllers;

import com.deephire.Dto.ConversationPartnerDto;
import com.deephire.Dto.MessageDTO;
import com.deephire.JWT.JwtUtils;
import com.deephire.Models.Message;
import com.deephire.Models.User;
import com.deephire.Repositories.MessageRepository;
import com.deephire.Repositories.UserRepository;
import com.deephire.Service.MessageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/message")
    public ResponseEntity<List<String>> getMessage() {
        return ResponseEntity.ok(Arrays.asList("Hello", "World"));
    }


    @PostConstruct
    public void init() {
        System.out.println("✅ MessageController is loaded!");
    }



    @GetMapping("/api/messages/contacts")
    public ResponseEntity<List<ConversationPartnerDto>> getContacts(Principal principal) {
        String currentUsername = principal.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Long currentUserId = currentUser.getId();
        List<ConversationPartnerDto> contacts = messageRepository.findConversationPartners(currentUserId);

        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/api/messages/conversation/{contactUsername}")
    public ResponseEntity<List<MessageDTO>> getConversation(
            @PathVariable String contactUsername,
            Principal principal) {

        String currentUsername = principal.getName();
        List<Message> conversation = messageRepository.findMessagesBetweenUsers(currentUsername, contactUsername);

        // Map Message entities to MessageDto objects

        List<MessageDTO> messageDtos = conversation.stream().map(msg -> new MessageDTO(
                msg.getContent(),
                msg.getTimestamp(),
                msg.getSender().getUsername(),
                msg.getReceiver().getUsername(),
                msg.getSender().getFirstName(),
                msg.getSender().getLastName()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(messageDtos);
    }




}
