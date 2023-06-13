package com.project.UrlJrr.service;

import com.project.UrlJrr.entity.User;
import com.project.UrlJrr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return new UserDetail(user.get());
        } else {
            throw new UsernameNotFoundException("아이디 또는 비밀번호가 일치 하지않습니다.");
        }
    }
}
