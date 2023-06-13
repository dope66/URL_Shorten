package com.project.UrlJrr.service;

import com.project.UrlJrr.dto.UserDto;
import com.project.UrlJrr.entity.User;
import com.project.UrlJrr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User register(UserDto userDto){
        Optional<User> existingUser = userRepository.findByUsername(userDto.getUsername());
        if(existingUser.isPresent()){
            throw  new IllegalStateException("이미 가입된 아이디 입니다.");
        }
        return userRepository.save(userDto.toEntity(passwordEncoder));
    }
    public Map<String,String> validatedHandling(Errors errors){
        Map<String, String> validatorResult = new HashMap<>();
        for (FieldError error : errors.getFieldErrors()) {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            validatorResult.put(fieldName, errorMessage);
        }
        return validatorResult;
    }
    public List<String> getAllUserEmails() {
        List<User> users = userRepository.findAll();
        List<String> userEmails = new ArrayList<>();

        for (User user : users) {
            userEmails.add(user.getEmail());
        }

        return userEmails;
    }



}
