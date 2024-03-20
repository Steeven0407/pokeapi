package com.api.pokeapi.payload;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse {

    private LocalDateTime tiempo = LocalDateTime.now();

    private String message;

    private String address;

    public ApiResponse(String message, String address) {
        this.message = message;
        this.address = address.replace("uri=", "");
    }
    
}
