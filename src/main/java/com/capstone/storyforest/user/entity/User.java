package com.capstone.storyforest.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String username;
    private String password;
    private LocalDate birthDate;

    /** 난이도 레벨: 1~3 */
    private int level;

    private int totalScore=0;

    private String accessToken;

    private String role;

    private int tier; //1~10

    @OneToMany(mappedBy = "user")
    private List<UserStory> userStories; // UserStory와의 관계
}
