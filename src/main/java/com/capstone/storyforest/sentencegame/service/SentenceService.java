package com.capstone.storyforest.sentencegame.service;

import com.capstone.storyforest.global.apiPaylod.code.status.ErrorStatus;
import com.capstone.storyforest.global.apiPaylod.exception.handler.SentenceHandler;
import com.capstone.storyforest.sentencegame.creativity.service.CreativityService;
import com.capstone.storyforest.sentencegame.dto.SentenceScoreResponseDTO;
import com.capstone.storyforest.sentencegame.dto.SentenceSubmitRequestDTO;

import com.capstone.storyforest.user.entity.User;
import com.capstone.storyforest.user.repository.UserRepository;
import com.capstone.storyforest.wordgame.entity.Word;
import com.capstone.storyforest.wordgame.repository.WordRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SentenceService {

    private final WordRepository wordRepository;
    private final UserRepository userRepository;
    private final ProfanityService profanityService;
    private final CreativityService creativityService;


    // 단어 7개 선택하는 로직(level 검증 포함)
    @Transactional(readOnly = true)
    public List<Word> get7Words(int level){
        if(level<1||level>3){
            throw new SentenceHandler(ErrorStatus.INVALID_LEVEL_SENTENCE);
        }

        List<Word> words=wordRepository.findRandomByLevel(level,7);
        if(words.size()<7){
            throw new SentenceHandler(ErrorStatus.NOT_ENOUGH_WORDS_SENTENCE);
        }

        return words;
    }


    // 문장 제출 -> 점수 계산 후 점수 누적하기
    @Transactional
    public SentenceScoreResponseDTO submitSentence(SentenceSubmitRequestDTO sentenceSubmitRequestDTO, User user) throws JsonProcessingException {


        // 레벨 검증하기
        if(sentenceSubmitRequestDTO.getLevel()<1||sentenceSubmitRequestDTO.getLevel()>3){
            throw new SentenceHandler(ErrorStatus.INVALID_LEVEL_SENTENCE);
        }

        // 비속어 검사하기
        if(profanityService.hasProfanity(sentenceSubmitRequestDTO.getSentenceText())){
            throw new SentenceHandler(ErrorStatus.PROFANITY_DETECTED);
        }

        /* 3. 단어 7개(문자열) 정규화 → DB 존재 여부 확인 */
        List<String> lowerTerms = sentenceSubmitRequestDTO.getWords()                 // "Apple" → "apple"
                .stream()
                .map(String::toLowerCase)
                .toList();


        List<Word> words = wordRepository.findByTermInIgnoreCase(lowerTerms);
        if (words.size() < 7)                                    // DB 미존재 ⇒ 잘못된 요청
            throw new SentenceHandler(ErrorStatus.NOT_ENOUGH_WORDS_SENTENCE);


        /*1. 7개 단어가 모두 문장에 포함되어 있는지 검사하기 */
        String senetenceLower= sentenceSubmitRequestDTO.getSentenceText().toLowerCase();

        boolean usedAll=true;  // 전부 포함 플래그
        for(Word word:words){
            if(!senetenceLower.contains(word.getTerm().toLowerCase())){
                usedAll=false; // 하나라도 빠지면 false
                break; // 더 볼 필요 없으니 탈출
            }
        }

        /* 정확 사용 = 7개 전부 사용했다고 가정 (고급 품사 검사는 추후 AI) */
        boolean correctUsage = usedAll;




        /* ─── 2. 창의성 판정 (정확 사용일 때만 검사) ─── */
        boolean creative = false;
        if (correctUsage) {
            creative = creativityService.isCreative(sentenceSubmitRequestDTO.getSentenceText());
        }

        // 3. 점수 계산하기
        int score;
        if(!correctUsage){
            score=10;
        }
        else if(creative){
            score=20;
        }
        else{
            score=15;
        }

        user.setTotalScore(user.getTotalScore()+score);
        userRepository.save(user);

        return new SentenceScoreResponseDTO(user.getTotalScore());
    }
}
