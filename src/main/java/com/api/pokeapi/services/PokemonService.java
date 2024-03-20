package com.api.pokeapi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.api.pokeapi.models.Pokemon;
import com.api.pokeapi.repositories.PokemonRepository;


@Service
public class PokemonService {

    private final PokemonRepository pokemonRepository;

    public PokemonService(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    public List<Pokemon> findAllPokemon() {
        return this.pokemonRepository.findAll();
    }

    public Optional<Pokemon> findPokemonById(Integer id){
        return this.pokemonRepository.findById(id);
    }

    public Optional<Pokemon> findPokemonByName(String name){
        return Optional.ofNullable(this.pokemonRepository.findByNombre(name));
    }

    public void createPokemon(Pokemon pokemon) {
        this.pokemonRepository.save(pokemon);
    }

    public void deletePokemon(Integer id) {
        this.pokemonRepository.deleteById(id);
    }
    
}
