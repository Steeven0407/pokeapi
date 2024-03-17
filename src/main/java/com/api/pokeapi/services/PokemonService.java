package com.api.pokeapi.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.api.pokeapi.models.Pokemon;
import com.api.pokeapi.repositories.PokemonRepository;

@Service
public class PokemonService {

    private final PokemonRepository pokemonRepository;

    public PokemonService(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    public List<Pokemon> findAllPokemon(){
        return this.pokemonRepository.findAll(); 
    }

    public Pokemon findPokemonById(Integer id){
        return this.pokemonRepository.findById(id)
                            .orElse(new Pokemon());
    }

    public Pokemon createPokemon(Pokemon pokemon) {
        return this.pokemonRepository.save(pokemon);
    }
    
}
