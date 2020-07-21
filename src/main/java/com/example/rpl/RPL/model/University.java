package com.example.rpl.RPL.model;

import lombok.Getter;

@Getter
public enum University {
    FIUBA ((long) 0, "Facultad de Ingenieria de la UBA", new String[]{"Ingenieria Informatica", "Licenciatura en Sistemas"});

    private final Long id;
    private final String name;
    private final String[] degrees;

    University(Long id, String name, String[] degrees) {
        this.id = id;
        this.name = name;
        this.degrees = degrees;
    }
}
