package com.deephire.Service;


import com.deephire.Dto.UserAdminDto;
import com.deephire.Dto.UserSearchDTO;
import com.deephire.Enums.ERole;
import com.deephire.Repositories.UserRepository;
import com.deephire.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private JavaMailSender mailSender;


    public User add(User user) { return userRepository.save(user); }

    public User find(Long id) { return userRepository.findById(id).get(); }

    public void delete(Long id) { userRepository.deleteById(id); }

    public User update(User user) { return userRepository.saveAndFlush(user); }

    public List<User> findAll() { return userRepository.findAll(); }
    public  User findByUsername(String username) {return  userRepository.findByUsername(username).get();}


    public List<UserAdminDto> findAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .filter(user -> user.getRoles().stream().anyMatch(role ->
                        role.getName() == ERole.ROLE_USER ||
                                role.getName() == ERole.ROLE_ADMINCOMPANY ||
                                role.getName() == ERole.ROLE_RECRUITER
                ))
                .map(user -> {
                    UserAdminDto userAdminDto = new UserAdminDto();
                    userAdminDto.setId(user.getId());
                    userAdminDto.setUsername(user.getUsername());
                    userAdminDto.setEmail(user.getEmail());
                    userAdminDto.setFirstName(user.getFirstName());
                    userAdminDto.setLastName(user.getLastName());
                    userAdminDto.setIsBanned(user.isBanned());
                    return userAdminDto;
                })
                .toList();
    }


    public void sendEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Welcome!");
        message.setText("Hello " + user.getUsername() + ",\n\nWelcome to our platform!");

        mailSender.send(message);
    }


    public List<UserSearchDTO> searchUsers(String searchText) {
        return userRepository.searchUsers(searchText);
    }


}
