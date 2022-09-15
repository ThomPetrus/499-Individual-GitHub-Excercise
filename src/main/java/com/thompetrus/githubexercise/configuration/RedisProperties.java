package com.thompetrus.githubexercise.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Basic config class that's autowired, populated in {@RedisConfig.class} by the values in application properties file.
 *
 * @author petrus
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisProperties {

    private String url;
    private int port;

}
