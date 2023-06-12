package com.ChemiChat.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDto {
    @NotNull
    private String username;

    @NotNull
    private String password;

}
