package com.api.pokeapi.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.api.pokeapi.exception.payload.ApiResponse;
import com.api.pokeapi.exception.validation.annotation.ValidFile;
import com.api.pokeapi.models.Pokemon;
import com.api.pokeapi.models.DTO.PokemonDTO;
import com.api.pokeapi.models.DTO.PokemonResponseDTO;
import com.api.pokeapi.services.CloudinaryService;
import com.api.pokeapi.services.PokemonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/pokeapi/v1")
@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequiredArgsConstructor
public class PokemonController {


    // Services
    private final PokemonService pokemonService;
    private final CloudinaryService cloudinaryService;


    @GetMapping("/findAll")
    public ResponseEntity<?> findAllPokemon(){
  
        List<PokemonResponseDTO> pokemonList = this.pokemonService.findAllPokemon()
            .stream()
            .map(pokemon -> {
                try{
                    return tPokemonResponseDTO(pokemon);
                }catch(JsonProcessingException e){
                    throw new PokemonConversionException("Error al procesar los datos del Pokémon", e);
                }
            })
            .toList();

        return ResponseEntity.ok(ApiResponse
            .builder()
            .flag(true)
            .code(200)
            .message("Info obtenida correctamente")
            .data(pokemonList)
            .build()
        );

    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> findPokemonById(@PathVariable Integer id) {

        try {

            Optional<Pokemon> pokemonOptional = this.pokemonService.findPokemonById(id);

            if (pokemonOptional.isEmpty()){
                throw new ResourceNotFoundException("Pokémon", "identificador", id);
            }

            Pokemon pokemon = pokemonOptional.get();
            PokemonResponseDTO pokemonResponseDto = tPokemonResponseDTO(pokemon);
            
            return ResponseEntity.ok(ApiResponse
                .builder()
                .flag(true)
                .code(200)
                .message("Pokémon obtenido correctamente")
                .data(pokemonResponseDto)
                .build()
            );
        
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
        @RequestParam("file") @Valid @ValidFile(message = "Archivo de imagen no valido") MultipartFile file,
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

            // upload the file
            Map<String, String> map = this.cloudinaryService.uploadFile(file, pokemon.getNombre());

            // set the file settings
            pokemon.setImageUrl(map.get("secure_url"));
            pokemon.setImageId(map.get("public_id"));

            // save the pokemon
            this.pokemonService.createPokemon(pokemon);

            return ResponseEntity.ok(ApiResponse
                .builder()
                .flag(true)
                .code(200)
                .message("Pokémon registrado correctamente")
                .data("No data provided")
                .build()
            );

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

        // get the pokemon
        Pokemon pokemon = pokemonOptional.get();

        // delete the file associated 
        this.cloudinaryService.delete(pokemon.getImageId());

        // delete the pokemon
        this.pokemonService.deletePokemon(id);

        return ResponseEntity.ok(ApiResponse
            .builder()
            .flag(true)
            .code(200)
            .message("Pokémon eliminado correctamente")
            .data("No data provided")
            .build()
        );

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePokemon(
        @PathVariable Integer id,
        @RequestParam("nombre") String nombre,
        @RequestParam("tipos") String tipos,
        @RequestParam("altura") float altura,
        @RequestParam("peso") float peso,
        @RequestParam("region") String region, 
        @RequestParam(required = false, name = "file") @Valid @ValidFile(message = "Archivo de imagen no valido") MultipartFile file,
        @Valid PokemonDTO pokemonDTO
    ) throws IOException{

        Optional<Pokemon> pokemonOptional = pokemonService.findPokemonById(id);

        if (pokemonOptional.isEmpty()){
            throw new ResourceNotFoundException("Pokémon", "identificador", id);
        }

        // get the pokemon
        Pokemon pokemon = pokemonOptional.get();
        String nombreAnt = pokemon.getNombre();

        if (pokemonService.findPokemonByName(nombre).isPresent() && !nombre.toLowerCase().equals(pokemon.getNombre().toLowerCase())){
            throw new ResourceAlreadyExistsException("Pokémon", "nombre", nombre);
        }

        // update data
        pokemon.setNombre(pokemonDTO.nombre());
        pokemon.setTipos(tipos);
        pokemon.setAltura(pokemonDTO.altura());
        pokemon.setPeso(pokemonDTO.peso());
        pokemon.setRegion(pokemonDTO.region());

        // update file if exists
        if (file != null){
            Map<String, String> result = this.cloudinaryService.updateFile(file, nombreAnt);
            pokemon.setImageUrl(result.get("secure_url"));
            pokemon.setImageId(result.get("public_id"));
        }

        // save changes
        this.pokemonService.createPokemon(pokemon);

        return ResponseEntity.ok(ApiResponse
            .builder()
            .flag(true)
            .code(200)
            .message("Pokémon actualizado correctamente")
            .data("No data provided")
            .build()
        );

    }


    // Converter methods

    private PokemonResponseDTO tPokemonResponseDTO(Pokemon pokemon) throws JsonProcessingException{

        
        ObjectMapper mapper = new ObjectMapper();

        List<String> tipos = mapper.readValue(pokemon.getTipos(), new TypeReference<List<String>>(){});

        return PokemonResponseDTO
            .builder()
            .id(pokemon.getId())
            .nombre(pokemon.getNombre())
            .tipos(tipos)
            .altura(pokemon.getAltura())
            .peso(pokemon.getPeso())
            .region(pokemon.getRegion())
            .sprite(pokemon.getImageUrl())
            .build();

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
