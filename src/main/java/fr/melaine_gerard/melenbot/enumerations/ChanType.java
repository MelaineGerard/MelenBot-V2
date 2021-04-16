package fr.melaine_gerard.melenbot.enumerations;

public enum ChanType {
    LOGS("logs"),
    SUGGESTIONS("suggestions"),
    WELCOME("welcome");

    private String name;

    private ChanType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
}
