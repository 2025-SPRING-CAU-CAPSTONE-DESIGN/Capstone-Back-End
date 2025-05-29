package com.capstone.storyforest.user.service;


import com.capstone.storyforest.global.apiPaylod.code.status.ErrorStatus;
import com.capstone.storyforest.global.apiPaylod.exception.handler.UserHandler;
import com.capstone.storyforest.global.jwt.JWTUtil;
import com.capstone.storyforest.story.dto.StoryRequestDTO;
import com.capstone.storyforest.story.entity.Story;
import com.capstone.storyforest.story.repository.StoryRepository;
import com.capstone.storyforest.user.dto.*;
import com.capstone.storyforest.user.entity.UserStory;
import com.capstone.storyforest.user.repository.UserRepository;
import com.capstone.storyforest.user.repository.UserStoryRepository;
import com.capstone.storyforest.wordgame.config.LevelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.capstone.storyforest.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserStoryRepository userStoryRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final StoryRepository storyRepository;

    public User joinProcess(JoinRequestDTO joinRequestDTO){

        String username=joinRequestDTO.getUsername();
        String password=joinRequestDTO.getPassword();
        String passwordConfirm=joinRequestDTO.getPasswordConfirm();
        LocalDate birthDate=joinRequestDTO.getBirthDate();
        // 레벨 계산
        int level=LevelUtil.calculateLevel(joinRequestDTO.getBirthDate());

        Boolean isExist=userRepository.existsByUsername(username);

        if (isExist) {
            throw new UserHandler(ErrorStatus.USERNAME_ALREADY_EXISTS); // username 중복 코드로 새로 만들면 더 좋음
        }

        if (!password.equals(passwordConfirm)) {
            throw new UserHandler(ErrorStatus.PASSWORDS_NOT_MATCH); // 비밀번호 불일치도 에러 코드 새로 만들어도 좋아
        }

        User user=new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setBirthDate(birthDate);
        user.setRole("ROLE_ADMIN");
        user.setTier(1);

        // 레벨 세팅
        user.setLevel(level);

        return userRepository.save(user);
    }


    @Transactional
    public UserLoginResponseDTO login(UserLoginRequestDTO userLoginRequestDTO){
        // 1. 이메일 검증
        User user = userRepository.findByUsername(userLoginRequestDTO.getUsername())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 2. 비밀번호 검증
        if (!bCryptPasswordEncoder.matches(userLoginRequestDTO.getPassword(), user.getPassword())) {
            throw new UserHandler(ErrorStatus.USER_WRONG_PASSWORD);
        }

        // 3. JWT 생성 (10시간 = 60 * 60 * 10 * 1000ms)
        String accessToken = jwtUtil.createJwt(user.getUsername(), user.getRole(), 60L *24* 60 * 60 * 1000);

        System.out.println("Generated JWT: " + accessToken);

        User updatedUser = user.toBuilder()
                .accessToken(accessToken)
                .build();

        userRepository.save(updatedUser);
        // 4. 응답 반환
        return new UserLoginResponseDTO(accessToken);

    }

    public User getUserInfo(String accessToken) {

        return userRepository.findByaccessToken(accessToken)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

    public boolean checkNickname(String username) {
        // 닉네임이 이미 사용 중인 경우에는 false 반환
        User user = userRepository.findByUsername(username).orElse(null);
        return user == null; // null이면 사용 가능, 아니면 사용 중
    }

    public GetTierResponseDTO getTierInfo(String accessToken) {
        User user = userRepository.findByaccessToken(accessToken)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int totalStories = userStoryRepository.countByUser(user); // 유저가 만든 스토리 수

        int tier = user.getTier();
        int lowerBound = (tier - 1) * 5;
        int storiesInCurrentTier = totalStories - lowerBound;
        int progressPercent = (int) ((storiesInCurrentTier / 5.0) * 100);
        int storiesToNextTier = (tier < 10) ? (5 - storiesInCurrentTier) : 0;
        int totalStory = totalStories;

        return new GetTierResponseDTO(tier, progressPercent, storiesToNextTier, totalStory);
    }

    public StoryResponseDTO getStory(int index, String accessToken) {
        if (index <= 0) {
            throw new IllegalArgumentException("Index must be 1 or greater");
        }

        User user = userRepository.findByaccessToken(accessToken)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable page = PageRequest.of(index - 1, 1); // 0-based
        List<UserStory> userStories = userStoryRepository.findTopNByUser(user, page);

        if (userStories.isEmpty()) {
            throw new RuntimeException("No user story found at the given index");
        }

        UserStory userStory = userStories.get(0);

        Story story = userStoryRepository.findStoryByUserStoryId(userStory.getId());
        if (story == null) {
            throw new RuntimeException("Story not found");
        }

        return new StoryResponseDTO(
                story.getId(),
                story.getTitle(),
                story.getContent(),
                story.getScore()
        );
    }


    @Transactional
    public StoryResponseDTO createUserStory(StoryRequestDTO request, String accessToken) {
        // 1) 토큰으로 User 조회
        User user = userRepository.findByaccessToken(accessToken)
                .orElseThrow(() -> new IllegalArgumentException("로그인 정보가 유효하지 않습니다."));

        // 2) Story 엔티티 저장
        Story story = Story.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .score(0)
                .build();
        Story saved = storyRepository.save(story);

        // 3) UserStory 연관관계 저장
        UserStory us = UserStory.builder()
                .user(user)
                .story(saved)
                .build();
        userStoryRepository.save(us);

        // 4) 응답용 DTO 변환
        return new StoryResponseDTO(
                saved.getId(),
                saved.getTitle(),
                saved.getContent(),
                saved.getScore()
        );
    }


}
