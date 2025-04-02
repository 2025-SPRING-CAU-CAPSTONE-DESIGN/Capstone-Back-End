package com.capstone.storyforest.user.service;


import com.capstone.storyforest.global.apiPaylod.code.status.ErrorStatus;
import com.capstone.storyforest.global.apiPaylod.exception.handler.UserHandler;
import com.capstone.storyforest.global.jwt.JWTUtil;
import com.capstone.storyforest.user.dto.JoinRequestDTO;
import com.capstone.storyforest.user.dto.UserLoginRequestDTO;
import com.capstone.storyforest.user.dto.UserLoginResponseDTO;
import com.capstone.storyforest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.capstone.storyforest.user.entity.User;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;

    public User joinProcess(JoinRequestDTO joinRequestDTO){

        String username=joinRequestDTO.getUsername();
        String password=joinRequestDTO.getPassword();
        String passwordConfirm=joinRequestDTO.getPasswordConfirm();
        LocalDate birthDate=joinRequestDTO.getBirthDate();

        Boolean isExist=userRepository.existsByUsername(username);

        if (isExist) {
            throw new UserHandler(ErrorStatus.USERNAME_ALREADY_EXISTS); // username 중복 코드로 새로 만들면 더 좋음
        }

        if (!password.equals(passwordConfirm)) {
            throw new UserHandler(ErrorStatus.PASSWORDS_NOT_MATCH); // 비밀번호 불일치도 에러 코드 새로 만들어도 좋아
        }

        User user=new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setBirthDate(birthDate);
        user.setRole("ROLE_ADMIN");

        return userRepository.save(user);
    }

    public UserLoginResponseDTO login(UserLoginRequestDTO userLoginRequestDTO){
        // 1. 이메일 검증
        User user = userRepository.findByUsername(userLoginRequestDTO.getUsername())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 2. 비밀번호 검증
        if (!bCryptPasswordEncoder.matches(userLoginRequestDTO.getPassword(), user.getPassword())) {
            throw new UserHandler(ErrorStatus.USER_WRONG_PASSWORD);
        }

        // 3. JWT 생성 (10시간 = 60 * 60 * 10 * 1000ms)
        String accessToken = jwtUtil.createJwt(user.getUsername(), user.getRole(), 60L *24* 60 * 60 * 1000);

        System.out.println("Generated JWT: " + accessToken);

        // 4. 응답 반환
        return new UserLoginResponseDTO(accessToken);

    }

}
