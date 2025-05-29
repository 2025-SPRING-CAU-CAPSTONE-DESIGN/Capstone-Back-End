package com.capstone.storyforest.story.service;

import com.capstone.storyforest.story.dto.StoryRequestDTO;
import com.capstone.storyforest.story.entity.Story;
import com.capstone.storyforest.story.repository.StoryRepository;
import com.capstone.storyforest.user.dto.StoryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final StoryRepository storyRepository;

    @Transactional
    public StoryResponseDTO createStory(StoryRequestDTO request) {
        // DTO → Entity
        Story story = Story.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .score(0)             // 새로 생성 시 초기 점수 설정
                .build();

        // 저장
        Story saved = storyRepository.save(story);

        // Entity → Response DTO
        return new StoryResponseDTO(
                saved.getId(),
                saved.getTitle(),
                saved.getContent(),
                saved.getScore()
        );
    }
}