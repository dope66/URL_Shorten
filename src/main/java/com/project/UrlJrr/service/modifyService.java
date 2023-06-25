package com.project.UrlJrr.service;


import com.project.UrlJrr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class modifyService {

    private final UserRepository userRepository;

    public boolean isEmailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }


}
