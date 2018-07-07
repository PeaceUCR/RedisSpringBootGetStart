package com.example.demo.config;


import com.example.demo.message.MessagePublisherService;
import com.example.demo.message.MessagePublisherServiceImpl;
import com.example.demo.message.MessageSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.stereotype.Component;

//config redis +jedis
@Component
public class JedisConfig {

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        //no this statement, connection exceptions here
        //https://blog.csdn.net/qq_22978533/article/details/76687582
        factory.afterPropertiesSet();
        return factory;
    }
    //RedisTemplate
    //The JedisConnectionFactory is made into a bean so that we can create a RedisTemplate to query data.
    //RedisTemplate that provides a high level abstraction for performing various Redis operations,
    // exception translation and serialization support
    //https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#redis:template

    //Serializer
    //the data stored in Redis is only bytes. While Redis itself supports various types, for the most part, these refer to the way the data is stored rather than what it represents.
    // It is up to the user to decide whether the information gets translated into strings or any other objects.
   //https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#redis:serializer
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        return template;
    }

    //Channel topic implementation (maps to a Redis channel).
    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("pubsub:queue");
    }

    @Bean
    MessagePublisherService redisPublisher() {
        return new MessagePublisherServiceImpl(redisTemplate(), topic());
    }


    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(new MessageSubscriber());
    }


    //RedisMessageListenerContainer is a class provided by Spring Data Redis which provides asynchronous behavior for Redis message listeners.
    @Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListener(), topic());
        return container;
    }
}
