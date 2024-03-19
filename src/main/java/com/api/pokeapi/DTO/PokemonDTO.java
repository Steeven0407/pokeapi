package com.api.pokeapi.DTO;

import java.util.List;

public record PokemonDTO(
    Integer id,
    String nombre,
    List<String> tipos,
    int altura,
    int peso,
    List<String> regiones,
    String sprite
) {
    
}
