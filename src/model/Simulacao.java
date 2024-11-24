package model;

public class Simulacao {
    private String cookie_user;
    private String name;
    private String cor;
    private int id;

    public Simulacao() {

    }

    public Simulacao(String name, String cor, String cookie, int id) {
        this.name = name;
        this.cor = cor;
        cookie_user = cookie;
        this.id = id;
    }
    
    public Simulacao(String name, String cor, String cookie) {
        this.name = name;
        this.cor = cor;
        cookie_user = cookie;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getCookie_user() {
        return cookie_user;
    }

    public void setCookie_user(String cookie_user) {
        this.cookie_user = cookie_user;
    }

    public String CreateJson() {
        return "{"
                + "\"nome\": \"" + name + "\","
                + "\"cor\": \"" + cor + "\","
                + "\"cookie_user\": \"" + cookie_user + "\""
                + "}";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
