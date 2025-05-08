package com.capstone.storyforest;

import org.junit.jupiter.api.Test;
import org.openkoreantext.processor.KoreanTokenJava;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;
import scala.collection.Seq;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OktMorphologyTest {

    /**
     * 문장에서 OKT 형태소 분석을 통해 어간(stem)과
     * 사전형 단어(dictForm)를 비교하여 포함 여부를 판단.
     */
    private boolean containsStem(String sentence, String dictForm) {
        // 1) 문장 정규화
        CharSequence normalized = OpenKoreanTextProcessorJava.normalize(sentence);
        // 2) 토크나이징
        Seq<KoreanTokenizer.KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(normalized);
        // 3) Java 리스트로 변환
        List<KoreanTokenJava> javaTokens = OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens);
        // 4) 각 토큰의 어간(stem)과 비교
        return javaTokens.stream()
                .anyMatch(token -> token.getStem().equals(dictForm));
    }

    @Test
    void testContainsStem_trueForConjugatedForm() {
        String sentence = "나는 도둑을 붙잡았다.";
        String dictForm = "붙잡다";
        assertTrue(
                containsStem(sentence, dictForm),
                "‘붙잡다’ 어간이 ‘붙잡았다’에서 추출되어야 합니다."
        );
    }

    @Test
    void testContainsStem_falseForNonExistentRoot() {
        String sentence = "나는 책을 읽었다.";
        String dictForm = "붙잡다";
        assertFalse(
                containsStem(sentence, dictForm),
                "‘붙잡다’ 어간이 없으므로 검출되지 않아야 합니다."
        );
    }

}
