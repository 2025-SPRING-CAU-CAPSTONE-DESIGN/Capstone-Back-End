package com.capstone.storyforest.user.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequestDTO {

    @NotBlank(message = "이름을 입력해주세요. ")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

}
