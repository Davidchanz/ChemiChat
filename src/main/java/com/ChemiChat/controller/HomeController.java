package com.ChemiChat.controller;

import com.ChemiChat.ChemiChatApplication;
import com.ChemiChat.RabbitQueueService;
import com.ChemiChat.dto.ApiResponse;
import com.ChemiChat.dto.ApiResponseSingleOk;
import com.ChemiChat.dto.SendMessageDto;
import com.ChemiChat.model.Message;
import com.ChemiChat.service.MessageService;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.ChemiChat.config.RabbitMqConfiguration.topicExchangeName;

@RestController
public class HomeController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private RabbitQueueService rabbitQueueService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/")
    public String home(Principal principal){
        return "Hello, " + principal.getName();
    }

    /*@PostMapping("/send")
    public void send(@RequestBody SendMessageDto sendMessageDto){
        Message message = new Message();
        message.setText(sendMessageDto.getMess());
        rabbitTemplate.convertAndSend(topicExchangeName, sendMessageDto.getRoute(), message);
    }*/

    @GetMapping("/update")
    public ResponseEntity<ApiResponseSingleOk> update(@RequestParam(value = "messageId") long id){
        Message message = messageService.getMessageById(id);
        System.out.println(message.getText());
        return new ResponseEntity<>(new ApiResponseSingleOk("Get Message", message.getText()), HttpStatus.OK);
    }

    @PostMapping("/create/room")
    public void createRoom(@RequestParam(value = "roomName") String roomName){
        rabbitQueueService.addNewQueue("room."+roomName, topicExchangeName, roomName);
    }

    @MessageMapping("/application")
    @SendTo("/all/messages")
    public SendMessageDto send(final SendMessageDto message) throws Exception {
        System.out.println(message);
        return message;
    }

    @MessageMapping("/private")
    public void sendToSpecificUser(@Payload SendMessageDto message) {
        simpMessagingTemplate.convertAndSend("/specific/" + message.getTo(), message);
    }

}
