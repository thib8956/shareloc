package fr.uha.shareloc.model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Account implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne
    private User user;
    @OneToOne
    private Colocation coloc;
    private Integer points;

    public Account(User user, Colocation colocation, int points) {
        this.user = user;
        this.coloc = colocation;
        this.points = points;
    }

    public Account() {
    }

    public User getUser() {
        return user;
    }

    public Colocation getColoc() {
        return coloc;
    }

    public Integer getPoints() {
        return points;
    }



}
