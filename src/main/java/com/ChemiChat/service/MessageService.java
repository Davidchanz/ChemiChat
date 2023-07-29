package com.ChemiChat.service;

import com.ChemiChat.model.Message;
import com.ChemiChat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public void save(Message message){
        messageRepository.save(message);
    }

    public Message getMessageById(Long id){
        return messageRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException("No Message with this Id!");//TODO
        });
    }
}
