package org.example.deephire.Service;

import org.example.deephire.Repositories.MessageRepository;
import org.example.deephire.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Message add(Message message) { return messageRepository.save(message); }

    public Message find(Long id) { return messageRepository.findById(id).get(); }

    public void delete(Long id) { messageRepository.deleteById(id); }

    public Message update(Message message) { return messageRepository.saveAndFlush(message); }

    public List<Message> findAll() { return messageRepository.findAll(); }
}
