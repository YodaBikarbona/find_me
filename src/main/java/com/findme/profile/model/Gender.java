package com.findme.profile.model;

public enum Gender {

    MALE("male"),
    FEMALE("female");

    private final String name;

    private Gender(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public static Gender findByName(String name) {
        for (Gender gender : Gender.values()) {
            if (gender.getName().equalsIgnoreCase(name)) {
                return gender;
            }
        }
        throw new IllegalArgumentException(String.format("No enum constant with name %s", name));
    }

}
