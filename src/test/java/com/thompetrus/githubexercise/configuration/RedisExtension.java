package com.thompetrus.githubexercise.configuration;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import redis.embedded.RedisServer;

public class RedisExtension implements BeforeAllCallback, AfterAllCallback {

    private static RedisServer redisServer = new RedisServer(6379);

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        redisServer.stop();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        redisServer.start();
    }
}


