package com.ChemiChat.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDto {
    @NotNull
    private String token;
}
