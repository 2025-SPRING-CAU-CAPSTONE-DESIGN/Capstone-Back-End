package com.capstone.storyforest.wordgame.service;

import com.capstone.storyforest.user.entity.User;
import com.capstone.storyforest.user.repository.UserRepository;
import com.capstone.storyforest.wordgame.dto.ScoreResponseDTO;
import com.capstone.storyforest.wordgame.dto.WordRequestDTO;
import com.capstone.storyforest.wordgame.entity.Word;
import com.capstone.storyforest.wordgame.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Word> getMixedWords(int level) {

        List<Word> list = new ArrayList<>();

        switch (level) {
            case 1 -> {
                list.addAll(wordRepository.findRandomByLevel(1, 15));
                list.addAll(wordRepository.findRandomByLevel(2,  5));
            }
            case 2 -> {
                list.addAll(wordRepository.findRandomByLevel(1,  5));
                list.addAll(wordRepository.findRandomByLevel(2, 10));
                list.addAll(wordRepository.findRandomByLevel(3,  5));
            }
            case 3 -> {
                list.addAll(wordRepository.findRandomByLevel(2,  5));
                list.addAll(wordRepository.findRandomByLevel(3, 15));
            }
            default -> throw new IllegalArgumentException("level must be 1~3");
        }

        Collections.shuffle(list);            // 카드 섞기용
        return list;                          // 총 20개
    }

    // 점수 누적 로직
    @Transactional
    public ScoreResponseDTO addRoundScore(WordRequestDTO scoreRequestDTO, User user) {
        int score;

        // 점수 계산 로직
        // 레벨이 같냐 다르냐에 따라 점수를 다르게 줌.
        if(scoreRequestDTO.getLevel()==user.getLevel()) score=15;
        else if(scoreRequestDTO.getLevel()<user.getLevel()) score=10;
        else score=20;

        // 유저의 누적점수 업데이트
        user.setTotalScore(user.getTotalScore()+score);
        userRepository.save(user);

        return new ScoreResponseDTO(user.getTotalScore());
    }
}
