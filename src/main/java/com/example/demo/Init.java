package com.example.demo;

import com.example.demo.message.MessagePublisherServiceImpl;
import com.example.demo.model.Item;
import com.example.demo.repo.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


@Component
public class Init implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private MessagePublisherServiceImpl messagePublisherService;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        //crud operations for repo
        Item i1 = new Item("123","apple");
        Item i2 = new Item("1234","orange");
        redisRepository.add(i1);
        redisRepository.add(i2);
        // forEach(action) method to iterate map
        redisRepository.findAllItems().forEach((k,v) -> System.out.println("Key = "
                + k + ", Value = " + v));

        //publish and listen
        messagePublisherService.publish("my first message pubblished");
    }
}
