package com.deephire.Service;

import com.deephire.Config.UserAuthenticationProvider;
import com.deephire.Dto.CredentialsDto;
import com.deephire.Dto.LoginResponseDto;
import com.deephire.Dto.SignUpDto;
import com.deephire.Dto.UserDto;
import com.deephire.Exceptions.AppException;
import com.deephire.Mappers.UserMapper;
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

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private  UserMapper userMapper;


    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByLogin(credentialsDto.login())
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {
            return userMapper.toUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }


    public UserDto register(SignUpDto userDto) {
        Optional<User> optionalUser = userRepository.findByLogin(userDto.login());

        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(userDto); // this is now safe
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.password())));

        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    public User add(User user) { return userRepository.save(user); }

    public User find(Long id) { return userRepository.findById(id).get(); }

    public void delete(Long id) { userRepository.deleteById(id); }

    public User update(User user) { return userRepository.saveAndFlush(user); }

    public List<User> findAll() { return userRepository.findAll(); }

    public UserDto findByLogin(String subject) {
        return userMapper.toUserDto(userRepository.findByLogin(subject).orElseThrow(()->new AppException("User not found",HttpStatus.NOT_FOUND)));
    }
}
