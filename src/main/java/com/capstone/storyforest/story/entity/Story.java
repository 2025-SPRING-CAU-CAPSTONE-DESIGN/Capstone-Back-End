package com.capstone.storyforest.story.entity;

import com.capstone.storyforest.user.entity.UserStory;
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
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    private int score;

    @OneToMany(mappedBy = "story")
    private List<UserStory> userStories; // UserStory와의 관계
}

