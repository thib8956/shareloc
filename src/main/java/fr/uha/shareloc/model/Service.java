package fr.uha.shareloc.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Service implements Serializable {
    @Id
    @GeneratedValue
    private int id;

    private String title;
    private String description;
    private int cost;
    private int upvotes;
    private int downvotes;
    private boolean accepted = false;
    private boolean achieved = false;
    @OneToOne private User creator;
    @OneToOne private User from = null; // Utilisateur réalisant le service
    @OneToMany private List<User> recipients;  // Liste des utilisateurs bénéficiant du service

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public int vote(int vote) {
        if (vote == 1) upvotes++;
        else downvotes--;
        return upvotes - downvotes;
    }

    public void setAccepted() {
        this.accepted = true;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public int getId() {
        return id;
    }

    public List<User> getRecipients() {
        return recipients;
    }

    public boolean isAchieved() {
        return achieved;
    }

    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }
}
