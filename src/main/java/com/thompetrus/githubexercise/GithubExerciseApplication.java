package com.thompetrus.githubexercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Yep - auto generated
 */
@SpringBootApplication(exclude = { RedisAutoConfiguration.class } )
public class GithubExerciseApplication {

	public static void main(String[] args) {
		SpringApplication.run(GithubExerciseApplication.class, args);
	}

}
