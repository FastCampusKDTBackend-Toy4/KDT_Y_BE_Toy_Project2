package com.kdt_y_be_toy_project2.global.jwt.repository;

import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class JwtRedisRepository implements JwtRepository {

    private final ValueOperations<String, String> opsForValue;

    public JwtRedisRepository(RedisTemplate<String, String> redisTemplate) {
        this.opsForValue = redisTemplate.opsForValue();
    }

    @Override
    public void save(JwtEntity entity) {
        opsForValue.set(entity.key(), entity.value(), entity.expiration(), TimeUnit.MILLISECONDS);
    }

    @Override
    public String getByKey(String key) {
        return opsForValue.get(key);
    }
}
