package com.ChemiChat;

import com.ChemiChat.model.User;

import java.util.Set;

public interface Room {
    void addClient();
    Set<User> getClients();
    User getClient(int id);
    int getRoomId();
}
