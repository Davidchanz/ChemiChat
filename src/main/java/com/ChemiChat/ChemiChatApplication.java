package com.ChemiChat;

import com.ChemiChat.config.RsaKeyProperties;
import com.ChemiChat.messagingrabbitmq.Receiver;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class ChemiChatApplication {
	public static void main(String[] args) {

		SpringApplication.run(ChemiChatApplication.class, args);
	}
}
