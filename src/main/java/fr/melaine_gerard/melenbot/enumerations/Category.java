package fr.melaine_gerard.melenbot.enumerations;

public enum Category {
    MUSIC("Musique"),
    UTILS("Utilitaire"),
    INFOS("Information"),
    OWNER("Owner"),
    OTHER("Autre");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
