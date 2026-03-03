package org.src.model;

import org.mindrot.jbcrypt.BCrypt;
import org.src.uprawnienia;

public class konto {
    private String haslo;
    private String login;
    private uprawnienia rola;
    private int id;

    public konto(int id,String login, String haslo, uprawnienia rola){
        this.login = login;
        this.haslo = haslo;
        this.rola = rola;
        this.id = id;
    }
    public String getLogin(){return login;}
    public String getHaslo(){return haslo;}
    public uprawnienia getRola(){return rola;}
    public void setHaslo(String nowehaslo){
        String zahashowane = BCrypt.hashpw(nowehaslo, BCrypt.gensalt());
        this.haslo = zahashowane;
    }
    public void setRola(uprawnienia nowaRola){this.rola = nowaRola;}
    public int getId(){return id;}

}
