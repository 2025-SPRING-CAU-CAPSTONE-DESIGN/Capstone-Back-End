package com.capstone.storyforest.user.controller;

import com.capstone.storyforest.user.dto.JoinRequestDTO;
import com.capstone.storyforest.user.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserJoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public String joinProcess(JoinRequestDTO joinRequestDTO){

        joinService.joinProcess(joinRequestDTO);

        return "ok";
    }
}
