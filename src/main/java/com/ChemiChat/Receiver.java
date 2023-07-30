package com.ChemiChat;

import com.ChemiChat.dto.SendMessageDto;
import com.ChemiChat.model.Message;

public interface Receiver {
    void receiveMessage(SendMessageDto message);
}
