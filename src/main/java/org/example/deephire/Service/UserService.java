package org.example.deephire.Service;

import org.example.deephire.Repositories.UserRepository;
import org.example.deephire.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User add(User user) { return userRepository.save(user); }

    public User find(Long id) { return userRepository.findById(id).get(); }

    public void delete(Long id) { userRepository.deleteById(id); }

    public User update(User user) { return userRepository.saveAndFlush(user); }

    public List<User> findAll() { return userRepository.findAll(); }
}
