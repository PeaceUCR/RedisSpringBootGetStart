package com.example.demo.repo;

import com.example.demo.model.Item;

import java.util.Map;


public interface RedisRepository {

    /**
     * Return all movies
     */
    Map<Object, Object> findAllItems();

    /**
     * Add key-value pair to Redis.
     */
    void add(Item movie);

    /**
     * Delete a key-value pair in Redis.
     */
    void delete(String id);

    /**
     * find a movie
     */
    Item findItem(String id);

}