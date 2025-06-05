package com.capstone.storyforest.friend.service;

import com.capstone.storyforest.friend.domain.FriendRequest;
import com.capstone.storyforest.friend.dto.FriendRequestDTO;
import com.capstone.storyforest.friend.dto.FriendRequestNotificationDTO;
import com.capstone.storyforest.friend.dto.FriendRequestResponseDTO;
import com.capstone.storyforest.friend.dto.FriendResponseDTO;
import com.capstone.storyforest.friend.repository.FriendRequestRepository;
import com.capstone.storyforest.global.apiPaylod.code.status.ErrorStatus;
import com.capstone.storyforest.global.apiPaylod.exception.handler.FriendHandler;
import com.capstone.storyforest.user.entity.User;
import com.capstone.storyforest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final UserRepository userRepository;
    private final FriendRequestRepository requestRepository;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public List<FriendRequestResponseDTO> getPendingRequests(User user) {
        return requestRepository.findAllByReceiverAndStatus(user, FriendRequest.Status.PENDING).stream()
                .map(fr -> new FriendRequestResponseDTO(fr.getId(), fr.getSender().getUsername()))
                .collect(Collectors.toList());
    }

    @Transactional
    public FriendRequestNotificationDTO sendRequest(User sender, FriendRequestDTO dto) {
        User receiver = userRepository.findByUsername(dto.getTargetUsername())
                .orElseThrow(() -> new FriendHandler(ErrorStatus.USER_NOT_FOUND));
        boolean exists = requestRepository.findAllByReceiverAndStatus(receiver, FriendRequest.Status.PENDING).stream()
                .anyMatch(fr -> fr.getSender().equals(sender));
        if (exists) {
            throw new FriendHandler(ErrorStatus.FRIEND_REQUEST_DUPLICATE);
        }
        FriendRequest fr = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendRequest.Status.PENDING)
                .build();
        requestRepository.save(fr);

        // 생성 시각 포함
        LocalDateTime sentAt = LocalDateTime.now();
        FriendRequestNotificationDTO notificationDto =
                new FriendRequestNotificationDTO(fr.getId(), sender.getUsername(), sentAt);

        notificationService.send(receiver.getId(), "friend-request", notificationDto);
        return notificationDto;
    }

    @Transactional
    public FriendRequest acceptRequest(User user, Long requestId) {
        FriendRequest fr = requestRepository.findById(requestId)
                .orElseThrow(() -> new FriendHandler(ErrorStatus._BAD_REQUEST));
        if (!fr.getReceiver().getId().equals(user.getId())) {
            throw new FriendHandler(ErrorStatus._FORBIDDEN);
        }
        fr.setStatus(FriendRequest.Status.ACCEPTED);
        return requestRepository.save(fr);
    }

    @Transactional
    public FriendRequest rejectRequest(User user, Long requestId) {
        FriendRequest fr = requestRepository.findById(requestId)
                .orElseThrow(() -> new FriendHandler(ErrorStatus._BAD_REQUEST));
        if (!fr.getReceiver().getId().equals(user.getId())) {
            throw new FriendHandler(ErrorStatus._FORBIDDEN);
        }
        fr.setStatus(FriendRequest.Status.REJECTED);
        return requestRepository.save(fr);
    }

    @Transactional(readOnly = true)
    public List<FriendResponseDTO> getFriends(User user) {
        Set<User> friends = new HashSet<>();
        requestRepository.findAllBySenderAndStatus(user, FriendRequest.Status.ACCEPTED)
                .forEach(fr -> friends.add(fr.getReceiver()));
        requestRepository.findAllByReceiverAndStatus(user, FriendRequest.Status.ACCEPTED)
                .forEach(fr -> friends.add(fr.getSender()));
        return friends.stream()
                .map(u -> new FriendResponseDTO(u.getId(), u.getUsername()))
                .collect(Collectors.toList());
    }

    @Transactional
    public User deleteFriend(User me, Long friendId) {
        // 1) 내가 보낸 ACCEPTED 요청에 해당하는 경우
        Optional<FriendRequest> sentOpt =
                requestRepository.findAllBySenderAndStatus(me, FriendRequest.Status.ACCEPTED)
                        .stream()
                        .filter(fr -> fr.getReceiver().getId().equals(friendId))
                        .findFirst();
        if (sentOpt.isPresent()) {
            User deletedUser = sentOpt.get().getReceiver();
            requestRepository.delete(sentOpt.get());
            return deletedUser;
        }

        // 2) 내가 받은 ACCEPTED 요청에 해당하는 경우
        Optional<FriendRequest> recvOpt =
                requestRepository.findAllByReceiverAndStatus(me, FriendRequest.Status.ACCEPTED)
                        .stream()
                        .filter(fr -> fr.getSender().getId().equals(friendId))
                        .findFirst();
        if (recvOpt.isPresent()) {
            User deletedUser = recvOpt.get().getSender();
            requestRepository.delete(recvOpt.get());
            return deletedUser;
        }

        throw new FriendHandler(ErrorStatus.FRIEND_NOT_FOUND);
    }

}
