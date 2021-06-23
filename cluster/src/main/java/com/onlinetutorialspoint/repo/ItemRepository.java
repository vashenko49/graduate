package com.onlinetutorialspoint.repo;

import com.onlinetutorialspoint.model.Data;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class ItemRepository {

    public static final String KEY = "ITEM";
    private RedisTemplate<String, Data> redisTemplate;
    private HashOperations hashOperations;

    public ItemRepository(RedisTemplate<String, Data> redisTemplate) {
        this.redisTemplate = redisTemplate;
        hashOperations = redisTemplate.opsForHash();
    }

    /*Getting all Items from tSable*/
    public Map<Integer, Data> getAllItems() {
        return hashOperations.entries(KEY);
    }

    /*Getting a specific item by item id from table*/
    public Data getItem(int itemId) {
        return (Data) hashOperations.get(KEY, itemId);
    }

    /*Adding an item into redis database*/
    public void addItem(Data item) {
        hashOperations.put(KEY, item.getId(), item);
    }

    /*delete an item from database*/
    public void deleteItem(int id) {
        hashOperations.delete(KEY, id);
    }

    /*update an item from database*/
    public void updateItem(Data item) {
        addItem(item);
    }
}
