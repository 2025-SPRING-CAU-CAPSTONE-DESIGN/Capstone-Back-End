package com.capstone.storyforest.sentencegame.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class SentenceSubmitRequestDTO {
    @Min(1) @Max(3)
    private int level;
    @Size(min = 7, max = 7)            // 정확히 7개 전달
    private List<@NotBlank String> words;   // 단어 문자열 목록
    @NotBlank
    private String sentenceText;

}