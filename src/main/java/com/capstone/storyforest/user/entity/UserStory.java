package com.capstone.storyforest.user.entity;

import com.capstone.storyforest.story.entity.Story;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user; // User 엔티티와의 관계

    @ManyToOne
    private Story story; // Story 엔티티와의 관계

}
