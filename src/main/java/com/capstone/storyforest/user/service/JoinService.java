package com.capstone.storyforest.user.service;


import com.capstone.storyforest.user.dto.JoinRequestDTO;
import com.capstone.storyforest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.capstone.storyforest.user.entity.User;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinRequestDTO joinRequestDTO){

        String username=joinRequestDTO.getUsername();
        String password=joinRequestDTO.getPassword();
        String passwordConfirm=joinRequestDTO.getPasswordConfirm();
        LocalDate birthDate=joinRequestDTO.getBirthDate();

        Boolean isExist=userRepository.existsByUsername(username);

        if(isExist){
            return;
        }

        if(!password.equals(passwordConfirm)){
            return;
        }

        User user=new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setBirthDate(birthDate);
        user.setRole("ROLE_ADMIN");

        userRepository.save(user);
    }

}
