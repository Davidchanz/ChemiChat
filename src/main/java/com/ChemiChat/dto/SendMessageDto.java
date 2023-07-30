package com.ChemiChat.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class SendMessageDto implements Serializable {
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
