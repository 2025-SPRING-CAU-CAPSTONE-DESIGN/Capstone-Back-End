package com.capstone.storyforest.wordgame.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String term;         // 단어

    @Column(nullable = false, length = 500)
    private String meaning;      // 뜻

    /** 난이도 = 레벨(1~3) */
    @Column(nullable = false)
    private int difficulty;
}