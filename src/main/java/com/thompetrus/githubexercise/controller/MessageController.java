package com.thompetrus.githubexercise.controller;

import com.thompetrus.githubexercise.repository.MessageRepo;
import com.thompetrus.githubexercise.model.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
public class MessageController {

    private MessageRepo messageRepo;

    public MessageController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }


    @GetMapping()
    public Mono<Message> getMessage(@RequestParam String key) {
        return messageRepo.getMessage(key);
    }


    @PutMapping()
    public Mono<Void> setMessage(@RequestParam String key, @RequestParam String message) {
        Message msg = Message.builder()
                .key(key)
                .message(message)
                .build();

       return messageRepo.setMessage(msg).then();
    }


    @GetMapping(path = "/all")
    public Flux<Message> getAllMessages() {
        return messageRepo.getAllMessages();
    }


    @DeleteMapping("/all")
    public void removeAllMessages() {
        messageRepo.removeAllMessages();
    }

}
