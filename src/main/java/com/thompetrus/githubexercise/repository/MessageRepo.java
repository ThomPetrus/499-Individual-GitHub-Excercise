package com.thompetrus.githubexercise.repository;

import com.thompetrus.githubexercise.exception.MessageException;
import com.thompetrus.githubexercise.exception.MessageNotFoundException;
import com.thompetrus.githubexercise.model.Message;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
public class MessageRepo {

    private final ReactiveValueOperations<String, Message> reactiveValueOperations;

    @Autowired
    public MessageRepo(ReactiveValueOperations<String, Message> reactiveValueOperations){
        this.reactiveValueOperations = reactiveValueOperations;
    }


    public Mono<Message> getMessage(@NonNull String key) {
        return reactiveValueOperations.get(key)
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


    public Mono<Boolean> setMessage(@NonNull Message message) {
        log.info(String.format("Updating message for key %s", message.getKey(), message));
        return reactiveValueOperations.set(message.getKey(), message)
                .onErrorMap(throwable ->{
                    String msg = String.format("Update message for key %s failed!", message.getKey());
                    log.info(msg, throwable);
                    return new MessageException(msg, throwable);
                });
    }
}
