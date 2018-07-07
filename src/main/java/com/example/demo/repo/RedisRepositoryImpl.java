package com.example.demo.repo;

import com.example.demo.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;


import java.util.Map;
import javax.annotation.PostConstruct;

@Repository
public class RedisRepositoryImpl implements RedisRepository {
    private static final String KEY = "Movie";

    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations hashOperations;

    @Autowired
    public RedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }
//Let’s take note of the init() method. In this method, we use a function named opsForHash(), which returns the operations performed on hash values bound to the given key.
// We then use the hashOps, which was defined in init(), for all of our CRUD operations.
//Returns the operations performed on hash values.
    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }

    public void add(final Item item) {
        hashOperations.put(KEY, item.getId(), item.getName());
    }

    public void delete(final String id) {
        hashOperations.delete(KEY, id);
    }

    public Item findItem(final String id){
        return (Item) hashOperations.get(KEY, id);
    }

    public Map<Object, Object> findAllItems(){
        return hashOperations.entries(KEY);
    }


}
