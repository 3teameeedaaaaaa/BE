package com.example.water.domain.chat.service;

import com.example.water.domain.chat.dto.RedisChatMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ChatRedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    private String messageKey(Long sessionId) {
        return "chat:session:" + sessionId + ":messages";
    }

    private String sequenceKey(Long sessionId) {
        return "chat:session:" + sessionId + ":sequence";
    }

    public int nextSequence(Long sessionId) {
        Long value = redisTemplate.opsForValue().increment(sequenceKey(sessionId));
        redisTemplate.expire(sequenceKey(sessionId), 1, TimeUnit.DAYS);
        return value == null ? 1 : value.intValue();
    }

    public void appendMessage(Long sessionId, RedisChatMessageDto messageDto) {
        redisTemplate.opsForList().rightPush(messageKey(sessionId), messageDto);
        redisTemplate.expire(messageKey(sessionId), 1, TimeUnit.DAYS);
    }

    public List<RedisChatMessageDto> getMessages(Long sessionId) {
        List<Object> rawList = redisTemplate.opsForList().range(messageKey(sessionId), 0, -1);

        if (rawList == null || rawList.isEmpty()) {
            return Collections.emptyList();
        }

        return rawList.stream()
                .map(obj -> objectMapper.convertValue(obj, RedisChatMessageDto.class))
                .toList();
    }

    public void clear(Long sessionId) {
        redisTemplate.delete(messageKey(sessionId));
        redisTemplate.delete(sequenceKey(sessionId));
    }
}