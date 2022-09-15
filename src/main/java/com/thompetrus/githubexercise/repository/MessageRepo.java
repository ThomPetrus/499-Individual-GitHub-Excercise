package com.thompetrus.githubexercise.repository;

import com.thompetrus.githubexercise.exception.MessageException;
import com.thompetrus.githubexercise.exception.MessageNotFoundException;
import com.thompetrus.githubexercise.model.Message;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveKeyCommands;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Class responsible for getting/setting messages in Redis
 */
@Slf4j
@Service
public class MessageRepo {

    private final static String NAME_SPACE = "message";

    private final ReactiveValueOperations<String, Message> reactiveValueOperations;
    private final ReactiveKeyCommands redisKeyCommands;

    @Autowired
    public MessageRepo(
        ReactiveValueOperations<String, Message> reactiveValueOperations,
        ReactiveKeyCommands redisKeyCommands
    ){
        this.reactiveValueOperations = reactiveValueOperations;
        this.redisKeyCommands = redisKeyCommands;
    }


    /**
     * Get a single message for a given key.
     *
     * @param key - Key to retrieve a message for
     * @return the requested message if found
     * @throws MessageException - if error occurs retrieving message
     * @throws MessageNotFoundException - if message not found
     */
    public Mono<Message> getMessage(@NonNull String key) {
        return reactiveValueOperations.get(String.format("%s:%s", NAME_SPACE, key))
                .onErrorMap(throwable ->{
                    String msg = String.format("Getting message for key %s failed!", key);
                    log.info(msg, throwable);
                    return new MessageException(msg, throwable);
                })
                .map(message -> {
                    if (Optional.ofNullable(message).isEmpty()) {
                        throw new MessageNotFoundException(String.format("Message not found for key: %s", key));
                    }
                    log.info(String.format("Retrieved message for key %s: %s", key, message));
                    return message;
                });
    }


    /**
     * Set a message in redis
     *
     * @param message - POJO of key and message
     * @return Boolean indicating success
     */
    public Mono<Boolean> setMessage(@NonNull Message message) {
        log.info(String.format("Updating message for key %s", message.getKey(), message));
        return reactiveValueOperations.set(String.format("%s:%s", NAME_SPACE, message.getKey()), message)
                .onErrorMap(throwable ->{
                    String msg = String.format("Update message for key %s failed!", message.getKey());
                    log.info(msg, throwable);
                    return new MessageException(msg, throwable);
                });
    }


    /**
     * Get all messages in cache
     *
     * @return All messages in cache
     */
    public Flux<Message> getAllMessages() {
        log.info("Getting all messages in cache.");
        return redisKeyCommands.keys(ByteBuffer.wrap(String.format("%s*", NAME_SPACE).getBytes()))
                .flatMapMany(Flux::fromIterable)
                .flatMap(byteBuffer -> reactiveValueOperations
                        .get(StandardCharsets.UTF_8.decode(byteBuffer).toString()));
    }


    /**
     * Remove all messages in cache
     */
    public void removeAllMessages() {
        redisKeyCommands.keys(ByteBuffer.wrap(String.format("%s*", NAME_SPACE).getBytes()))
                .flatMapMany(Flux::fromIterable)
                .flatMap(byteBuffer -> reactiveValueOperations
                        .delete(StandardCharsets.UTF_8.decode(byteBuffer).toString()))
                .collectList().block();
    }

}
