package com.ChemiChat.messagingrabbitmq;

import java.util.concurrent.CountDownLatch;

import com.ChemiChat.dto.SendMessageDto;
import com.ChemiChat.model.Message;
import com.ChemiChat.service.MessageService;
import com.ChemiChat.service.RoomService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static com.ChemiChat.config.RabbitMqConfiguration.topicExchangeName;

@Component
public class Receiver implements com.ChemiChat.Receiver {

    private CountDownLatch latch = new CountDownLatch(1);

    @Autowired
    private MessageService messageService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private RoomService roomService;

    @Override
    @RabbitListener(id = topicExchangeName)
    public void receiveMessage(SendMessageDto messageDto) {
        System.out.println("Received <" + messageDto.getText() + ">");

        Message message = new Message();
        message.setText(messageDto.getText());
        message.setFrom(messageDto.getFrom());
        message.setTo(messageDto.getTo());
        Message mess = messageService.save(message);
        roomService.sendMessage(message.getTo(), mess);
        simpMessagingTemplate.convertAndSend("/specific/" + message.getTo(), message);

        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
