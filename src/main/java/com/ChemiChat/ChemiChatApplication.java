package com.ChemiChat;

import com.ChemiChat.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class ChemiChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChemiChatApplication.class, args);
	}
}
