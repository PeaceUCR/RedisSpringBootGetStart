package com.example.demo.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class MessagePublisherServiceImpl  implements  MessagePublisherService {


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //https://docs.spring.io/spring-data/data-redis/docs/current/api/org/springframework/data/redis/listener/ChannelTopic.html
    @Autowired
    private ChannelTopic topic;


    public MessagePublisherServiceImpl() {
    }

    public MessagePublisherServiceImpl(final RedisTemplate<String, Object> redisTemplate, final ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    //Publishes the given message to the given channel.
    @Override
    public void publish(String message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
