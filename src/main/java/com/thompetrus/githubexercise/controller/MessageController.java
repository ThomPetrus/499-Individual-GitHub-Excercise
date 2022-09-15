package com.thompetrus.githubexercise.controller;

import com.thompetrus.githubexercise.repository.MessageRepo;
import com.thompetrus.githubexercise.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Basic Controller to retrieve the messages from redis.
 */
@RestController
public class MessageController {

    private MessageRepo messageRepo;

    @Autowired
    public MessageController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }


    /**
     * Get a messages stored in redis based on the key provided.
     *
     * @param key - requested key
     * @return Message - key and Message stored at that key
     */
    @GetMapping()
    public Mono<Message> getMessage(@RequestParam String key) {
        return messageRepo.getMessage(key);
    }


    /**
     * Store a message at the key provided. If key exists, the message will be overridden.
     *
     * @param key - Key to store the message at.
     * @param message - Message to store
     * @return nada
     */
    @PutMapping()
    public Mono<Void> setMessage(@RequestParam String key, @RequestParam String message) {
        Message msg = Message.builder()
                .key(key)
                .message(message)
                .build();

       return messageRepo.setMessage(msg).then();
    }


    /**
     * Retrieve all messages currently stored in the cache.
     *
     * @return all messages
     */
    @GetMapping(path = "/all")
    public Flux<Message> getAllMessages() {
        return messageRepo.getAllMessages();
    }


    /**
     * Simply delete all messages stored in cache.
     */
    @DeleteMapping("/all")
    public void removeAllMessages() {
        messageRepo.removeAllMessages();
    }

}
