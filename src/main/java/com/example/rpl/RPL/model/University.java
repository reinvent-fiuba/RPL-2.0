package com.example.rpl.RPL.model;

import lombok.Getter;

@Getter
public enum University {
    FIUBA ((long) 0, "Facultad de Ingeniería de la UBA", new String[]{"Ingeniería en Informática", "Licenciatura en Sistemas", "Ingeniería Civil", "Ingeniería de Alimentos", "Ingeniería Electricista", "Ingeniería Electrónica", "Ingeniería en Agrimensura", "Ingeniería en Petróleo", "Ingeniería Industrial", "Ingeniería Mecánica", "Ingeniería Naval y Mecánica", "Ingeniería Química"});

    private final Long id;
    private final String name;
    private final String[] degrees;

    University(Long id, String name, String[] degrees) {
        this.id = id;
        this.name = name;
        this.degrees = degrees;
    }
}
