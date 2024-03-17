package com.api.pokeapi.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
    private String tipo;

    @Column(
        nullable = false
    )
    private int altura;

    private int peso;

    @Column(
        columnDefinition = "TEXT"
    )
    private String region;

    @Column(
        nullable = false
    )
    private String sprite;

    public Pokemon() {
    }

    public Pokemon(String nombre, String tipo, int altura, int peso, String region, String sprite) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.altura = altura;
        this.peso = peso;
        this.region = region;
        this.sprite = sprite;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    
    
}
