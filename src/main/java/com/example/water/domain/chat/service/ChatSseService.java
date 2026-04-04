package com.example.water.domain.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ChatSseService {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter connect(Long sessionId) {
        SseEmitter emitter = new SseEmitter(60L * 60 * 1000); // 1시간
        emitters.put(sessionId, emitter);

        emitter.onCompletion(() -> {
            emitters.remove(sessionId);
            log.info("SSE complete sessionId={}", sessionId);
        });

        emitter.onTimeout(() -> {
            emitters.remove(sessionId);
            emitter.complete();
            log.info("SSE timeout sessionId={}", sessionId);
        });

        emitter.onError((e) -> {
            emitters.remove(sessionId);
            log.warn("SSE error sessionId={}, error={}", sessionId, e.getMessage());
        });

        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected"));
        } catch (IOException e) {
            emitters.remove(sessionId);
            throw new RuntimeException("SSE 연결 실패");
        }

        return emitter;
    }

    public void send(Long sessionId, String eventName, Object data) {
        SseEmitter emitter = emitters.get(sessionId);
        if (emitter == null) {
            return;
        }

        try {
            emitter.send(SseEmitter.event()
                    .name(eventName)
                    .data(data));
        } catch (IOException e) {
            emitters.remove(sessionId);
            emitter.complete();
        }
    }
}