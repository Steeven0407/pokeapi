package com.api.pokeapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.pokeapi.models.Pokemon;

public interface PokemonRepository extends JpaRepository<Pokemon, Integer>{

    Pokemon findByNombre(String nombre);
    
}
