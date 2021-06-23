package com.streaming.main;

import com.streaming.main.model.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class Application {
    //    private static String URL = "ws://localhost:8080/streaming";
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    RedisTemplate<String, Data> redisTemplate() {
        RedisTemplate<String, Data> redisTemplate = new RedisTemplate<String, Data>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
