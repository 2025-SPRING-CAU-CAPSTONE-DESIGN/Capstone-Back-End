package com.capstone.storyforest.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class JoinDto {

    @NotEmpty(message="닉네임을 입력하셔야 합니다.")
    private String username;

    @NotEmpty(message = "비밀번호를 입력하셔야 합니다.")
    private String password;

    @NotEmpty(message = "비밀번호 확인을 입력하셔야 합니다.")
    private String passwordConfirm;

    @NotEmpty(message = "생년월일을 입력하셔야 합니다.")
    private LocalDate birthDate;

}
