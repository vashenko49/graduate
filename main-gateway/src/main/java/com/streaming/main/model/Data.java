package com.streaming.main.model;

import lombok.Builder;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@lombok.Data
@Builder
@RedisHash("data")
public class Data {
    Long numberMessage;
    @Indexed
    String id;
    String messageListJSON;
    Operation operation;
    Boolean isFinal;
}
