package fr.melaine_gerard.melenbot.enumerarions;

public enum Category {
    MUSIC("Musique"),
    UTILS("Utilitaire"),
    INFOS("Information"),
    OTHER("Autre");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
