package fr.melaine_gerard.melenbot.enumerations;

public enum Category {
    INFOS("Information"),
    UTILS("Utilitaire"),
    MUSIC("Musique"),
    GAME("Jeu"),
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
