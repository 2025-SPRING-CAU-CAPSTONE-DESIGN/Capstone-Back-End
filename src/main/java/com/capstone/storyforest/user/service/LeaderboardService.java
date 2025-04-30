package com.capstone.storyforest.user.service;

import com.capstone.storyforest.global.apiPaylod.code.status.ErrorStatus;
import com.capstone.storyforest.global.apiPaylod.exception.handler.LeaderboardHandler;
import com.capstone.storyforest.user.dto.LeaderboardEntryDTO;
import com.capstone.storyforest.user.entity.User;
import com.capstone.storyforest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final UserRepository userRepository;

    /*page:0을 base로 하고, size 제한은 50*/
    public List<LeaderboardEntryDTO> getLeaderboard(int page,int size) {

        if (page < 0)              throw new LeaderboardHandler(ErrorStatus.INVALID_PAGE);
        if (size < 1 || size > 50) throw new LeaderboardHandler(ErrorStatus.INVALID_SIZE);


        Page<User> users=userRepository.findAllByOrderByTotalScoreDescIdAsc(
                PageRequest.of(page,Math.min(size,50)));

       List<LeaderboardEntryDTO> board=new ArrayList<>();
       int rank=page*size+1; // 현재 페이지의 첫 번째 순위

        for(User user:users.getContent()){
            board.add(new LeaderboardEntryDTO(
                    rank,
                    user.getUsername(),
                    user.getLevel(),
                    user.getTotalScore()
            ));
            rank++;
        }
        return board;
    }

}
