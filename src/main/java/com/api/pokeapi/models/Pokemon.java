package com.api.pokeapi.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Pokemones")
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(
        length = 20,
        nullable = false,
        unique = true

    )
    private String nombre;

    @Column(
        columnDefinition = "TEXT"
    )
    private String tipos;

    @Column(
        nullable = false
    )
    private float altura;

    @Column(
        nullable = false
    )
    private float peso;

    private String region;

    @Column(
        nullable = false
    )
    private String imageUrl;

    private String imageId;

}
