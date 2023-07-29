package com.ChemiChat.messagingrabbitmq;

import java.util.concurrent.CountDownLatch;

import com.ChemiChat.model.Message;
import com.ChemiChat.service.MessageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ChemiChat.config.RabbitMqConfiguration.topicExchangeName;

@Component
public class Receiver implements com.ChemiChat.Receiver {

    private CountDownLatch latch = new CountDownLatch(1);

    @Autowired
    private MessageService messageService;

    @Override
    @RabbitListener(id = topicExchangeName)
    public void receiveMessage(Message message) {
        System.out.println("Received <" + message.getText() + ">");
        messageService.save(message);
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
