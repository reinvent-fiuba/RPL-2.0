package com.example.rpl.RPL.model;

import lombok.Getter;

@Getter
public enum Language {

    C("c", "std11"),
    PYTHON3("python", "3.7"),
    JAVA("java", "11");

    private final String name;
    private final String version;

    Language(final String name, final String version) {
        this.name = name;
        this.version = version;
    }

    public static Language getByName(String name) {
        for (Language addressTaskStatus : Language.values()) {
            if (addressTaskStatus.name.equals(name)) {
                return addressTaskStatus;
            }
        }
        return Language.C;
    }

    public static Language getByNameAndVersion(String langId) {
        for (Language addressTaskStatus : Language.values()) {
            if (addressTaskStatus.getNameAndVersion().equals(langId)) {
                return addressTaskStatus;
            }
        }
        return Language.C;
    }

    public String getNameAndVersion() {
        return String.format("%s_%s", this.name, this.version);
    }

}
