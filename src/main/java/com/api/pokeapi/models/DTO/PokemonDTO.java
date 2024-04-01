package com.api.pokeapi.models.DTO;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import com.api.pokeapi.exception.validation.annotation.ValidTipos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record PokemonDTO(

    Integer id,

    @NotBlank(message = "El nombre del Pokémon es obligatorio")
    @Length(min = 3, max = 20, message = "El nobre del Pokémon debe contener entre 3 y 20 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ]+$", message = "El nombre debe pokémon no debe contener caracteres especiales")
    String nombre,

    @ValidTipos(message = "El pokémon solo puede contener entre 1 y 3 tipos")
    List<String> tipos,

    @Positive(message = "La altura no puede ser negativa")
    @Digits(integer = Integer.MAX_VALUE, fraction = 1, message = "La altura no debe sobrepasar los 2 puntos decimales")
    float altura,

    @PositiveOrZero(message = "El peso solo puede ser 0 o positivo")
    @Digits(integer = Integer.MAX_VALUE, fraction = 1, message = "El peso no debe sobrepasar los 2 puntos decimales")
    float peso,

    String region,

    @URL(message = "El sprite debe corresponder con una URL válida")
    String sprite
    
) {

    public void setTipos(List<String> tipos) {
        this.tipos.clear();
        this.tipos.addAll(tipos);
    }
    
}
