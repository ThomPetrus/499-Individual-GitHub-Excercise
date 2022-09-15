package com.thompetrus.githubexercise.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * POJO for Message Class persisted in Redis.
 */
@Data
@Builder
@AllArgsConstructor
public class Message {
    private String key;
    private String message; 
}
