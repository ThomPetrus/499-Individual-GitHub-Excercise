package com.thompetrus.githubexercise.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thompetrus.githubexercise.model.Message;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Data
@Configuration
public class RedisConfig {

    @Bean
    @ConfigurationProperties(prefix = "redis")
    public RedisCacheProperties redisCacheProperties() {
        return new RedisCacheProperties();
    }


    @Bean
    public ReactiveRedisConnectionFactory redisConnectionFactory(RedisCacheProperties redisCacheProperties) {
        return new LettuceConnectionFactory(
                redisCacheProperties.getUrl(),
                redisCacheProperties.getPort());
    }


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
