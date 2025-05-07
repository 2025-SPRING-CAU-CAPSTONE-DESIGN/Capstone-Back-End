package com.capstone.storyforest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.KoreanTokenJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;
import scala.collection.Seq;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class DictWordMatcherTest {

    /* ────────────────────────────────────────────
       OKT 형태소 분석으로 문장의 어간(stem) 집합 추출
       ──────────────────────────────────────────── */
    private Set<String> extractStems(String sentence) {
        CharSequence norm = OpenKoreanTextProcessorJava.normalize(sentence);
        Seq<KoreanTokenizer.KoreanToken> tokens =
                OpenKoreanTextProcessorJava.tokenize(norm);
        List<KoreanTokenJava> javaTokens =
                OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens);
        return javaTokens.stream()
                .map(KoreanTokenJava::getStem)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    /* ────────────────────────────────────────────
       사전형 단어(dictTerm)가 문장에 사용됐는지 판정
       동사(…다)와 명사 구분 로직
       ──────────────────────────────────────────── */
    private boolean containsDictWord(String dictTerm, Set<String> stems) {
        final String term = dictTerm.toLowerCase();   // effectively‑final

        if (term.endsWith("다")) {             // ── 동사 ──
            /* 1) stem 집합에 사전형 그대로 들어오는 경우 */
            if (stems.contains(term)) return true;

            /* 2) 혹시 '붙잡았' 등 접두 형태만 있는 경우 대비 */
            String base = term.substring(0, term.length() - 1);
            return stems.stream().anyMatch(s -> s.startsWith(base));
        } else {                              // ── 명사 ──
            return stems.contains(term) ||
                    stems.stream().anyMatch(s -> s.startsWith(term));
        }
    }

    /* ────────────────────────────────────────────
       테스트 케이스
       ──────────────────────────────────────────── */





    @Test
    @DisplayName("동사 활용형 매칭: 붙잡다 → 붙잡았다 true")
    void verbStemMatch_true() {
        String sentence = "노동자들이 경영진을 붙잡아서";
        Set<String> stems = extractStems(sentence);
        assertTrue(
                containsDictWord("붙잡다", stems),
                "'붙잡다' 어간이 '붙잡았다'에서 추출돼야 한다."
        );
    }
}
