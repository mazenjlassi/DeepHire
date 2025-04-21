package com.deephire.Service;


import com.deephire.Repositories.UserRepository;
import com.deephire.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;





    public User add(User user) { return userRepository.save(user); }

    public User find(Long id) { return userRepository.findById(id).get(); }

    public void delete(Long id) { userRepository.deleteById(id); }

    public User update(User user) { return userRepository.saveAndFlush(user); }

    public List<User> findAll() { return userRepository.findAll(); }
    public  User findByUsername(String username) {return  userRepository.findByUsername(username).get();}


}
