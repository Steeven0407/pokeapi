package com.api.pokeapi.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.pokeapi.models.Pokemon;
import com.api.pokeapi.services.PokemonService;

@RestController
@RequestMapping("/pokeapi/v1")
public class PokemonController {

    private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/pokemons")
    public List<Pokemon> findAllPokemon(){
        return this.pokemonService.findAllPokemon();
    }

    @GetMapping("/pokemons/{id}")
    public Pokemon findPokemonById(@PathVariable Integer id){
        return this.pokemonService.findPokemonById(id);
    }

    @PostMapping("/pokemons")
    public Pokemon createPokemon(@RequestBody Pokemon pokemon){
        return this.pokemonService.createPokemon(pokemon);
    }
    
}
