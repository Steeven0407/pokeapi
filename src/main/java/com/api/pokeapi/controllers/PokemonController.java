package com.api.pokeapi.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.pokeapi.exception.PokemonConversionException;
import com.api.pokeapi.exception.ResourceAlreadyExistsException;
import com.api.pokeapi.exception.ResourceNotFoundException;
import com.api.pokeapi.exception.validation.annotation.ValidFile;
import com.api.pokeapi.models.Pokemon;
import com.api.pokeapi.models.DTO.PokemonDTO;
import com.api.pokeapi.services.CloudinaryService;
import com.api.pokeapi.services.PokemonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/pokeapi/v1")
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class PokemonController {

    private final PokemonService pokemonService;
    private final CloudinaryService cloudinaryService;

    public PokemonController(PokemonService pokemonService, CloudinaryService cloudinaryService) {
        this.pokemonService = pokemonService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAllPokemon(){

        
        List<PokemonDTO> pokemonList = this.pokemonService.findAllPokemon()
            .stream()
            .map(t -> {
                try {
                    return toPokemonDto(t);
                } catch (JsonProcessingException e) {
                    throw new PokemonConversionException("Error al procesar los datos del Pokémon", e);
                }
            })
            .toList();

        return ResponseEntity.ok(pokemonList);

        

    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> findPokemonById(@PathVariable Integer id) {

        try {

            Optional<Pokemon> pokemonOptional = this.pokemonService.findPokemonById(id);

            if (pokemonOptional.isEmpty()){
                throw new ResourceNotFoundException("Pokémon", "identificador", id);
            }

            Pokemon pokemon = pokemonOptional.get();
            PokemonDTO pokemonDto = toPokemonDto(pokemon);
            
            return ResponseEntity.ok(pokemonDto);
        
        } catch(JsonProcessingException e){
            throw new PokemonConversionException("Error al procesar los datos de los Pokémones", e);
        }
        
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPokemon(
        @RequestParam("nombre") String nombre,
        @RequestParam("tipos") String tipos,
        @RequestParam("altura") float altura,
        @RequestParam("peso") float peso,
        @RequestParam("region") String region, 
        @RequestParam("file") @Valid @ValidFile MultipartFile file,
        @Valid PokemonDTO pokemonDTO
    ) throws IOException {

        try{

            if (pokemonService.findPokemonByName(nombre).isPresent()){
                throw new ResourceAlreadyExistsException("Pokémon", "nombre", nombre);
            }

            ObjectMapper mapper = new ObjectMapper();
            List<String> list_tipos = mapper.readValue(tipos, new TypeReference<List<String>>(){});
            pokemonDTO.setTipos(list_tipos);

            Pokemon pokemon = this.toPokemon(pokemonDTO);

            Map<String, String> map = this.cloudinaryService.uploadFile(file, file.getOriginalFilename().split("\\.")[0]);

            pokemon.setImageUrl(map.get("secure_url"));
            pokemon.setImageId(map.get("public_id"));
            this.pokemonService.createPokemon(pokemon);

            Map<String, String> response = new HashMap<>();
            response.put("message", "El pokémon fue registrado correctamente");

            return ResponseEntity.ok(response);

       }catch(JsonProcessingException e){
            throw new PokemonConversionException("Error al intentar crear el Pokémon", e);
        }
     
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePokemon(@PathVariable Integer id) throws IOException{

        Optional<Pokemon> pokemonOptional = pokemonService.findPokemonById(id);

        if (pokemonOptional.isEmpty()){
            throw new ResourceNotFoundException("Pokémon", "identificador", id);
        }

        Pokemon pokemon = pokemonOptional.get();

        this.cloudinaryService.delete(pokemon.getImageId());

        this.pokemonService.deletePokemon(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Pokémon eliminado correctamente"); 

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePokemon(
        @PathVariable Integer id,
        @RequestParam("nombre") String nombre,
        @RequestParam("tipos") String tipos,
        @RequestParam("altura") float altura,
        @RequestParam("peso") float peso,
        @RequestParam("region") String region, 
        @RequestParam(required = false, name = "file") @Valid @ValidFile MultipartFile file,
        @Valid PokemonDTO pokemonDTO
    ) throws IOException{

        Optional<Pokemon> pokemonOptional = pokemonService.findPokemonById(id);

        if (pokemonOptional.isEmpty()){
            throw new ResourceNotFoundException("Pokémon", "identificador", id);
        }

        Pokemon pokemon = pokemonOptional.get();

        if (pokemonService.findPokemonByName(nombre).isPresent() && !nombre.toLowerCase().equals(pokemon.getNombre().toLowerCase())){
            throw new ResourceAlreadyExistsException("Pokémon", "nombre", nombre);
        }

        pokemon.setNombre(pokemonDTO.nombre());
        pokemon.setTipos(tipos);
        pokemon.setAltura(pokemonDTO.altura());
        pokemon.setPeso(pokemonDTO.peso());
        pokemon.setRegion(pokemonDTO.region());

        if (file != null){
            Map<String, String> result = this.cloudinaryService.updateFile(file, file.getOriginalFilename().split("\\.")[0]);
            pokemon.setImageUrl(result.get("secure_url"));
            pokemon.setImageId(result.get("public_id"));
        }

        this.pokemonService.createPokemon(pokemon);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Pokémon actualizado correctamente"); 

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    private PokemonDTO toPokemonDto(Pokemon pokemon) throws JsonProcessingException{

        ObjectMapper mapper = new ObjectMapper();

        List<String> tipos = mapper.readValue(pokemon.getTipos(), new TypeReference<List<String>>(){});

        return new PokemonDTO(
            pokemon.getId(), 
            pokemon.getNombre(), 
            tipos, 
            pokemon.getAltura(), 
            pokemon.getPeso(), 
            pokemon.getRegion(), 
            pokemon.getImageUrl()
        );

    }

    private Pokemon toPokemon(PokemonDTO dto) throws JsonProcessingException{

        ObjectMapper mapper = new ObjectMapper();

        String tipos = mapper.writeValueAsString(dto.tipos());

        Pokemon pokemon = new Pokemon(); 
        pokemon.setNombre(dto.nombre());
        pokemon.setTipos(tipos); 
        pokemon.setAltura(dto.altura());
        pokemon.setPeso(dto.peso());
        pokemon.setRegion(dto.region()); 

        return pokemon;

    }
    
}
