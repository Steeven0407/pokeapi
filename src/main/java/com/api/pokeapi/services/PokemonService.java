package com.api.pokeapi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.api.pokeapi.models.Pokemon;
import com.api.pokeapi.repositories.PokemonRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PokemonService {

    // Instancia del repositorio
    private final PokemonRepository pokemonRepository;

    /**
     * Método encargado de listar todos los pokémones en BD
     * @return List<Pokemon>
     */
    public List<Pokemon> findAllPokemon() {
        return this.pokemonRepository.findAll();
    }

    /**
     * Método encargado de encontrar un pokémon dado un id especifico
     * @param id
     * @return Optional<Pokemon>
     */
    public Optional<Pokemon> findPokemonById(Integer id){
        return this.pokemonRepository.findById(id);
    }

    /**
     * Método que busca un pokémon dado su nombre
     * @param name
     * @return Optional<Pokemon>
     */
    public Optional<Pokemon> findPokemonByName(String name){
        return Optional.ofNullable(this.pokemonRepository.findByNombre(name));
    }

    /**
     * Método encargado de crear un nuevo pokémon
     * @param pokemon
     */
    public void createPokemon(Pokemon pokemon) {
        this.pokemonRepository.save(pokemon);
    }

    /**
     * Métpdp encargado de eliminar un pokémon
     * @param id
     */
    public void deletePokemon(Integer id) {
        this.pokemonRepository.deleteById(id);
    }
    
}
