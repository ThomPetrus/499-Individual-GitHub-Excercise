package com.thompetrus.githubexercise.repository;


import com.thompetrus.githubexercise.configuration.RedisExtension;
import com.thompetrus.githubexercise.model.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@SpringBootTest
@ActiveProfiles("test")
@ExtendWith({RedisExtension.class})
public class MessageRepoTest {

    @Autowired
    MessageRepo messageRepo;

    @Autowired
    ReactiveValueOperations<String, Message> reactiveValueOperations;

    @Test
    void shouldBeAbleToSetAndGetMessageFromRedis() {
        // given
        final String key = "blaKey";
        final String msg = "blaMessage";

        Message expectedMessage = Message.builder()
                .key(key)
                .message(msg)
                .build();

        // when message for key is present
        messageRepo.setMessage(expectedMessage).block();
        Message persistedMessage = messageRepo.getMessage(expectedMessage.getKey()).block();

        // then
        Assertions.assertEquals(expectedMessage, persistedMessage);
    }

    @Test
    void shouldGetAllMessagesFromRedis() {
        // given
        final String key = "blaKey";
        final String msg = "blaMessage";

        List<Message> expectedMessages = new ArrayList<>();
        for(int i = 0; i < 100; i++) {

            String newKey= String.format("%s%d", key, i);
            String newMsg = String.format("%s%d", msg, i);

            Message expectedMessage = Message.builder()
                    .key(newKey)
                    .message(newMsg)
                    .build();

            expectedMessages.add(expectedMessage);
            messageRepo.setMessage(expectedMessage).block();
        }

        // when
        List<Message> persistedMessages = messageRepo.getAllMessages().collectList().block();

        // then
        Assertions.assertEquals(expectedMessages.size(), persistedMessages.size());
    }


    @Test
    void shouldDeleteAllMessagesInRedis() {
        // given
        final String key = "blaKey";
        final String msg = "blaMessage";

        List<Message> expectedMessages = new ArrayList<>();
        for(int i = 0; i < 100; i++) {

            String newKey= String.format("%s%d", key, i);
            String newMsg = String.format("%s%d", msg, i);

            Message expectedMessage = Message.builder()
                    .key(newKey)
                    .message(newMsg)
                    .build();

            expectedMessages.add(expectedMessage);
            messageRepo.setMessage(expectedMessage).block();
        }

        // when
        messageRepo.removeAllMessages();

        // then
        List<Message> persistedMessages = messageRepo.getAllMessages().collectList().block();
        Assertions.assertEquals(0, persistedMessages.size());
    }
}
