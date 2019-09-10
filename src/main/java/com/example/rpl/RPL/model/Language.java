package com.example.rpl.RPL.model;

public enum Language {

    C("C", "std11"),
    PYTHON3("python", "3.7"),
    JAVA("java", "11");

    private String name;
    private String version;

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

    public static Language getByNameAndVersion(String lang_id) {
        for (Language addressTaskStatus : Language.values()) {
            if (addressTaskStatus.getNameAndVersion().equals(lang_id)) {
                return addressTaskStatus;
            }
        }
        return Language.C;
    }

    public String getNameAndVersion() {
        return String.format("%s_%s", this.name, this.version);
    }

}
