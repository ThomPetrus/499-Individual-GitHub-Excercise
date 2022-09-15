package com.thompetrus.githubexercise.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thompetrus.githubexercise.model.Message;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveKeyCommands;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Beans Config to all things related to the redis cache access
 * @author petrus
 */
@Data
@Configuration
public class RedisConfig {

    /**
     * Pulls the config values from application.properties
     *
     * @author petrus
     * @return RedisProperties - POJO object w/ Redis props
     */
    @Bean
    @ConfigurationProperties(prefix = "redis")
    public RedisProperties redisCacheProperties() {
        return new RedisProperties();
    }


    /**
     * Override Lettuce connection factory to enable connection to anything other than defaults.
     * i.e. host:localhost; port: 6397
     *
     * @author petrus
     * @param redisCacheProperties - POJO class containing the redis connection params
     * @return ReactiveRedisConnectionFactory
     */
    @Bean
    public ReactiveRedisConnectionFactory redisConnectionFactory(RedisProperties redisCacheProperties) {
        return new LettuceConnectionFactory(
                redisCacheProperties.getUrl(),
                redisCacheProperties.getPort());
    }

    /**
     * Key commands used for the multi get commands.
     *
     * @author petrus
     * @param reactiveRedisConnectionFactory
     * @return ReactiveKeyCommands - key commands afforded by the reactive connection.
     */
    @Bean
    public ReactiveKeyCommands redisKeyCommands(ReactiveRedisConnectionFactory
                                                   reactiveRedisConnectionFactory) {
        return reactiveRedisConnectionFactory.getReactiveConnection().keyCommands();
    }


    /**
     * Set up bean for the required redis commands.
     *
     * @author petrus
     * @param factory - Connection factory for redis connections
     * @param redisObjectMapper - possibly unnecessary object mapper
     * @return reactiveValueOperations - Basic Redis operations to retrieve values from Redis cache
     */
    @Bean
    public ReactiveValueOperations<String, Message> reactiveMessageOperations(
        ReactiveRedisConnectionFactory factory,
        ObjectMapper redisObjectMapper
    ) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Message> valueSerializer = new Jackson2JsonRedisSerializer<>(Message.class);
        valueSerializer.setObjectMapper(redisObjectMapper);
        RedisSerializationContext.RedisSerializationContextBuilder<String, Message> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, Message> context = builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(factory, context).opsForValue();
    }

}
