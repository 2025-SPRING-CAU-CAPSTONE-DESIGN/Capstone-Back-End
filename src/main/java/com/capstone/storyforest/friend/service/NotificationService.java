package com.capstone.storyforest.friend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {

    // 선언과 동시에 초기화 → null 방지
    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    /**
     * 유저별 SSE 구독 등록
     */
    public SseEmitter subscribe(Long userId) {
        // 0L 대신 Long.MAX_VALUE을 주면 “사실상 무제한” 타임아웃
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        // computeIfAbsent 결과를 변수에 담아 사용
        List<SseEmitter> userEmitters = emitters.computeIfAbsent(userId, id -> new ArrayList<>());
        userEmitters.add(emitter);

        emitter.onCompletion(() -> removeEmitter(userId, emitter));
        emitter.onTimeout(()    -> removeEmitter(userId, emitter));
        emitter.onError(err    -> removeEmitter(userId, emitter));

        return emitter;
    }

    /**
     * 특정 유저에게 이벤트 푸시
     */
    public void send(Long userId, String eventName, Object data) {
        List<SseEmitter> userEmitters = emitters.getOrDefault(userId, Collections.emptyList());
        Iterator<SseEmitter> it = userEmitters.iterator();

        while (it.hasNext()) {
            SseEmitter emitter = it.next();
            try {
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(data));
            } catch (IOException e) {
                // 에러 난 emitter는 목록에서 제거
                it.remove();
            }
        }
    }

    private void removeEmitter(Long userId, SseEmitter emitter) {
        List<SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            userEmitters.remove(emitter);
        }
    }
}
