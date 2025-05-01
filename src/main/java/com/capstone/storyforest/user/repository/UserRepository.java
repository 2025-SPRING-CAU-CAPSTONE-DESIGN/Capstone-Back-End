package com.capstone.storyforest.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.storyforest.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByaccessToken(String accessToken);

    // 누적 점수-> id 순으로 정렬&페이징하는
    // 리더보드를 위한 함수
    Page<User> findAllByOrderByTotalScoreDescIdAsc(Pageable pageable);

}
