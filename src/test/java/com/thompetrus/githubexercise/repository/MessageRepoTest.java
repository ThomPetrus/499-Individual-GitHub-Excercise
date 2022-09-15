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
}
