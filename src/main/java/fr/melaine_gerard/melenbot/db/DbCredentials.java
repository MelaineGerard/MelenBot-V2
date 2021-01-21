package fr.melaine_gerard.melenbot.db;

public class DbCredentials {
    private String host;
    private String user;
    private String pass;
    private String databaseName;
    private int port;


    public DbCredentials(String host, String user, String pass, String databaseName, int port){
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.databaseName = databaseName;
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String toUri(){
        final StringBuilder sb = new StringBuilder();
        sb.append("jdbc:mysql://")
                .append(host)
                .append(":")
                .append(port)
                .append("/")
                .append(databaseName);
        return sb.toString();
    }
}
