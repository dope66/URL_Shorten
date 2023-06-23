package com.project.UrlJrr.service;

import com.project.UrlJrr.dto.UserDto;
import com.project.UrlJrr.entity.User;
import com.project.UrlJrr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final JavaMailSender javaMailSender;

    public User register(UserDto userDto) {
        Optional<User> existingUser = userRepository.findByUsername(userDto.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("이미 가입된 아이디 입니다.");
        }
        Optional<User> existingEmail = userRepository.findByEmail(userDto.getEmail());
        if (existingEmail.isPresent()) {
            throw new IllegalStateException("이미 가입된 이메일 주소입니다.");
        }

        return userRepository.save(userDto.toEntity(passwordEncoder));
    }

    public  User update(UserDto userDto){
        // 이메일 중복 체크
//        Optional<User> existingEmail = userRepository.findByEmail(userDto.getEmail());
//        if (existingEmail.isPresent() && ) {
//            throw new IllegalStateException("존재하는 이메일 입니다.");
//        }

        // 비밀번호 체크
        if(!checkPassword(userDto.getPassword()))
            throw new IllegalStateException("비밀번호는 8~16자 영문 대소문자, 숫자, 특수문자를 사용해야 합니다.");
        
        // 수정 및 저장하기
        Optional<User> user = userRepository.findByUsername(userDto.getUsername());
        if(user.isPresent()){
            // 신입/경력, 비밀번호, 스킬스택 변경 후 원본에 저장
            user.get().setExperience(userDto.getExperience());
            user.get().setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.get().setSkillStack(userDto.getSkillStack());
            return userRepository.save(user.get());
        }
        throw new IllegalStateException("존재하지 않는 아이디 입니다..");
    }

    public boolean checkPassword(String newPassword){
        //return "error: 비밀번호는 8~16자 영문 대소문자, 숫자, 특수문자를 사용해야 합니다.";
        return !newPassword.matches("(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}");
    }

    public Map<String, String> validatedHandling(Errors errors) {
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

    public User getUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    // 현재 로그인한 사용자의 이름을 가지고오기위한것.
    public String getUsername() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return authentication.getName();
    }

    public String changePassword(String currentPassword, String newPassword, String confirmPassword) {
        String username = getUsername();
        User user = getUserByUsername(username);
        //        현재 비밀번호 확인 하기
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return "error : 현재 비밀번호가 올바르지 않습니다.";
        }
//       새로운 비밀번호 정규식 검증
        if (!newPassword.matches("(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}")) {
            return "error: 비밀번호는 8~16자 영문 대소문자, 숫자, 특수문자를 사용해야 합니다.";
        }
//      비밀번호 일치 여부
        if (!newPassword.equals(confirmPassword)) {
            return "error: 새로운 비밀번호가 일치하지 않습니다.";

        }
//        비밀번호 업데이트
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "비밀번호가 성공적으로 변경되었습니다.";

    }


    public String userEamilCheck(String email, String username) {
        if(email.isEmpty() || username.isEmpty()){
            return "빈칸을 채워주세요 ";
        }

        Optional<User> user = userRepository.findByEmailAndUsername(email, username);
        if(!user.isPresent()){
            return "등록되지않은 사용자 입니다.";
        }
        return "";



    }

    public void resetPasswordAndSendEmail(String email, String username) {
        // db에서 찾고 없으면 메세지
        User user = userRepository.findByEmailAndUsername(email, username).orElseThrow(() -> new RuntimeException("User not found"));
        String temporaryPassword = generateTemporaryPassword();
        String hashedPassword = passwordEncoder.encode(temporaryPassword);
        user.setPassword(hashedPassword);
        userRepository.save(user);
        sendTemporaryPasswordEmail(email, temporaryPassword);

    }

    public void sendTemporaryPasswordEmail(String recipientEmail, String temporaryPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("임시 비밀번호 보내 드립니다.");
        message.setText("임시 비밀 번호 입니다.: " + temporaryPassword);
        javaMailSender.send(message);
    }

    private String generateTemporaryPassword() {
//       임시 비번 생성로직 구현해야됨
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String temporaryPassword = "";
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            temporaryPassword += charSet[idx];
        }
        return temporaryPassword;
    }

}
