package com.project.UrlJrr.controller;

import com.project.UrlJrr.dto.UserDto;
import com.project.UrlJrr.dto.ajaxDTO;
import com.project.UrlJrr.entity.User;
import com.project.UrlJrr.exception.ErrorResponse;
import com.project.UrlJrr.exception.UserException;
import com.project.UrlJrr.repository.UserRepository;
import com.project.UrlJrr.service.UserService;
import com.project.UrlJrr.service.modifyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@ResponseBody
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;
    private final modifyService modifyService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PutMapping("/modify")
    public ajaxDTO modifySave(@RequestBody UserDto userData) {

        // userData 객체를 사용하여 파라미터에 접근
        String username = userData.getUsername();
        String email = userData.getEmail();
        System.out.println(email);
        String skillStack = userData.getSkillStack();
        String experience = userData.getExperience();

        ajaxDTO ajaxDTO = new ajaxDTO();
        User user = userService.getUserByUsername(userService.getUsername());

        // 접속 중 username 과 전달 받은 username 일치 여부 확인
        if (userService.getUsername().equals(username)) {

            // 이메일 중복 여부 확인
            // 이메일이 존재하지않는 이메일이며 내가가진 이메일 주소가 아니라면 update
            if (!user.getEmail().equals(email) && modifyService.isEmailExist(email)) {
                ajaxDTO.setTitle("실패");
                ajaxDTO.setMessage("변경할 수 없는 이메일 입니다.");
                ajaxDTO.setFlag("error");
                return ajaxDTO;
            }
            // 저장 처리
//            user.setPassword(passwordEncoder.encode(password1));
            user.setEmail(email);
            user.setSkillStack(skillStack);
            user.setExperience(experience);
            userRepository.save(user);

            ajaxDTO.setTitle("성공");
            ajaxDTO.setMessage("3초뒤 메인페이지로 이동합니다.");
            ajaxDTO.setFlag("success");
            return ajaxDTO;


        } else {
            // 현재 접속한 username 을 요청한 것이 아님
            ajaxDTO.setTitle("실패");
            ajaxDTO.setMessage("요청하신 아이디가 아닙니다.");
            ajaxDTO.setFlag("error");
            return ajaxDTO;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto) {
        try {
            User newUser = userService.register(userDto);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (UserException ex) {
            int errorCode = ex.getExceptionType().getErrorCode();
            String errorMessage = ex.getExceptionType().getErrorMessage();
            ErrorResponse errorResponse = new ErrorResponse(errorCode, errorMessage);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response){
        userService.deleteUser(id);
//         로그아웃 처리를 위해 SecurityContextLogoutHandler를 사용하여 세션 무효화
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, null);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}



