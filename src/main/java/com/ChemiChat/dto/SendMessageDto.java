package com.ChemiChat.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SendMessageDto {
    private String mess;
    private String route;

    @Override
    public String toString() {
        return "SendMessageDto{" +
                "mess='" + mess + '\'' +
                ", route='" + route + '\'' +
                '}';
    }
}
