package com.capstone.storyforest.sentencegame.service;

import com.capstone.storyforest.global.apiPaylod.code.status.ErrorStatus;
import com.capstone.storyforest.global.apiPaylod.exception.handler.SentenceHandler;
import com.capstone.storyforest.sentencegame.appropriateness.dto.AppropriatenessResult;
import com.capstone.storyforest.sentencegame.appropriateness.service.AppropriatenessService;
import com.capstone.storyforest.sentencegame.creativity.service.CreativityService;
import com.capstone.storyforest.sentencegame.dto.SentenceScoreResponseDTO;
import com.capstone.storyforest.sentencegame.dto.SentenceSubmitRequestDTO;
import com.capstone.storyforest.sentencegame.filtering.service.ProfanityService;
import com.capstone.storyforest.user.entity.User;
import com.capstone.storyforest.user.repository.UserRepository;
import com.capstone.storyforest.wordgame.entity.Word;
import com.capstone.storyforest.wordgame.repository.WordRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.openkoreantext.processor.KoreanTokenJava;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scala.collection.Seq;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SentenceService {

    private final WordRepository wordRepository;
    private final UserRepository userRepository;
    private final ProfanityService profanityService;
    private final CreativityService creativityService;
    private final AppropriatenessService appropriatenessService;

    // ───────────────── OKT 형태소 분석: 문장의 어간 집합 추출
    private Set<String> extractStems(String sentence) {
        CharSequence norm = OpenKoreanTextProcessorJava.normalize(sentence);
        Seq<KoreanTokenizer.KoreanToken> tokens =
                OpenKoreanTextProcessorJava.tokenize(norm);
        List<KoreanTokenJava> javaTokens =
                OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens);

        return javaTokens.stream()
                .map(t -> {
                    String stem = t.getStem();
                    return (stem != null && !stem.isEmpty() ? stem : t.getText()).toLowerCase();
                })
                .collect(Collectors.toSet());
    }

    // ───────────────── 표제어(명사/동사) ↔ 어간 매칭
    private boolean containsDictWord(String dictTerm, Set<String> stems) {
        String term = dictTerm.toLowerCase();

        if (term.endsWith("다")) {            // 동사·형용사
            if (stems.contains(term)) return true;
            String base = term.substring(0, term.length() - 1);
            return stems.stream().anyMatch(s -> s.startsWith(base));
        } else {                             // 명사
            return stems.contains(term) ||
                    stems.stream().anyMatch(s -> s.startsWith(term)   // ② stems가 더 길 때
                            || term.startsWith(s)); // ③ stems가 더 짧을 때 ★ 추가
        }
    }

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


        /* 1. 7개 단어가 모두 문장에 포함되어 있는지 검사하기 ─ 변경 ★ */
        Set<String> stems = extractStems(sentenceSubmitRequestDTO.getSentenceText());  // 형태소 분석 → 어간 집합

        boolean usedAll = true;          // 전부 포함 플래그
        for (Word word : words) {
            if (!containsDictWord(word.getTerm(), stems)) {   // ★ 표제어/활용형 매칭
                usedAll = false;
                break;
            }
        }



        System.out.println("===== DEBUG =====");
        System.out.println("stems : " + stems);          // 형태소 분석 결과
        for (Word w : words) {
           boolean ok = containsDictWord(w.getTerm(), stems);
           System.out.println(w.getTerm() + " -> " + ok);
        }
        System.out.println("=================");



        // 3. 점수 계산하기
        int score;
        if (!usedAll) {
            score = 10;
        } else {
            List<String> terms = words.stream()
                    .map(Word::getTerm)
                    .toList();
            AppropriatenessResult result = appropriatenessService.evaluate(sentenceSubmitRequestDTO.getSentenceText(), terms);

            if (Boolean.TRUE.equals(result.getIsAppropriate())) {
                score = 15;
            } else {
                boolean creative = creativityService.isCreative(sentenceSubmitRequestDTO.getSentenceText());
                score = creative ? 20 : 10;
            }
        }

        user.setTotalScore(user.getTotalScore()+score);
        userRepository.save(user);

        return new SentenceScoreResponseDTO(user.getTotalScore());
    }
}
