package fr.melaine_gerard.melenbot.enumerations;

public enum CommandCategory {
    INFOS("Information"),
    UTILS("Utilitaire"),
    MUSIC("Musique"),
    FUN("Fun"),
    GAME("Jeu"),
    MODS("Modération"),
    OWNER("Owner"),
    OTHER("Autre");

    private final String name;

    CommandCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
