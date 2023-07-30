package com.ChemiChat.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SendMessageDto {
    private String text;
    private String to;
    private String from;

    @Override
    public String toString() {
        return "SendMessageDto{" +
                "text='" + text + '\'' +
                ", to='" + to + '\'' +
                ", from='" + from + '\'' +
                '}';
    }
}
