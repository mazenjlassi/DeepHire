package com.deephire.Service;

import com.deephire.Models.User;
import com.deephire.Repositories.MessageRepository;
import com.deephire.Models.Message;
import com.deephire.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;


    public Message add(Message message) { return messageRepository.save(message); }

    public Message find(Long id) { return messageRepository.findById(id).get(); }

    public void delete(Long id) { messageRepository.deleteById(id); }

    public Message update(Message message) { return messageRepository.saveAndFlush(message); }

    public List<Message> findAll() { return messageRepository.findAll(); }
}
