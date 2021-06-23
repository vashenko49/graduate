package com.onlinetutorialspoint;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@org.springframework.context.annotation.Configuration
public class Configuration {
    public static String queueName = "spring-boot";

    //
    @Bean
    public Queue myQueue() {
        return new Queue(queueName, false);
    }
}
