package com.example.userserivce.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class RedisTestService {

    StringRedisTemplate redisTemplate;

    public void save(String key, String value){
        redisTemplate.opsForValue().set(key,value);
    }

    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }

}
