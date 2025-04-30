package com.capstone.storyforest.wordgame.service;

import com.capstone.storyforest.user.entity.User;
import com.capstone.storyforest.user.repository.UserRepository;
import com.capstone.storyforest.wordgame.dto.ScoreRequestDTO;
import com.capstone.storyforest.wordgame.dto.ScoreResponseDTO;
import com.capstone.storyforest.wordgame.entity.Word;
import com.capstone.storyforest.wordgame.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final UserRepository userRepository;

    // 난이도를 1~3 중에서 랜덤으로 선택
    @Transactional
    public List<Word>  getRandomWords(int count) {
        int difficulty= ThreadLocalRandom.current().nextInt(1,4);

        return wordRepository.findRandomByLevel(difficulty,count);
    }

    // 점수 누적 로직
    @Transactional
    public ScoreResponseDTO addRoundScore(ScoreRequestDTO scoreRequestDTO, User user) {
        int score;

        // 점수 계산 로직
        // 레벨이 같냐 다르냐에 따라 점수를 다르게 줌.
        if(scoreRequestDTO.getDifficulty()==user.getLevel()) score=15;
        else if(scoreRequestDTO.getDifficulty()<user.getLevel()) score=10;
        else score=20;

        // 유저의 누적점수 업데이트
        user.setTotalScore(user.getTotalScore()+score);
        userRepository.save(user);

        return new ScoreResponseDTO(user.getTotalScore());
    }
}
