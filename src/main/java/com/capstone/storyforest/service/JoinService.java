package com.capstone.storyforest.service;

import com.capstone.storyforest.dto.JoinDto;
import com.capstone.storyforest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.capstone.storyforest.entity.User;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDto joinDto){

        String username=joinDto.getUsername();
        String password=joinDto.getPassword();
        String passwordConfirm=joinDto.getPasswordConfirm();
        LocalDate birthDate=joinDto.getBirthDate();

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
