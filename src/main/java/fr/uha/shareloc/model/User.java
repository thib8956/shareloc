package fr.uha.shareloc.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class User implements Serializable {

    @Id
    private String login;
    private String email;
    private String password;
    private String firstname;
    private String lastname;

    public User() {
    }

    public User(String login, String password, String firstname, String lastname) {
        this.login = login;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
