package com.api.pokeapi.models.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PokemonResponseDTO {

    Integer id;

    String nombre;

    List<String> tipos;

    float altura;

    float peso;

    String region;

    String sprite;
    
}
