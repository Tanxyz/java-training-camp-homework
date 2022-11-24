package com.tan.homework.micrometer.redis.config;

import com.tan.homework.micrometer.redis.beans.RedisTemplateWrapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfiguration {


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setStringSerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisTemplateWrapper<String, Object> redisTemplateWrapper(RedisTemplate<String, Object> redisTemplate, ApplicationContext applicationContext) {
        return new RedisTemplateWrapper<>(redisTemplate, applicationContext);
    }


}
