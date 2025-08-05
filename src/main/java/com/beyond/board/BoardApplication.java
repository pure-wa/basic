package com.beyond.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession // 세션 스토리지로 Redis를 사용하겠다는 설정
@SpringBootApplication
@EnableScheduling // 스케줄러 사용 시 필요한 어노테이션
public class BoardApplication {

	public static void main(String[] args) {

		SpringApplication.run(BoardApplication.class, args);
	}

}

w