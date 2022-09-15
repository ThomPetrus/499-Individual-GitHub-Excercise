package com.thompetrus.githubexercise.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisCacheProperties {

    private String url;
    private int port;

}
