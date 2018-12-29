package fr.uha.shareloc.model;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
public class AchievedService implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @OneToOne private User from;
    @OneToMany private List<User> to;
    @OneToOne private Service service;
    private Date date;
    private URI picture;
    private boolean valid;

    public AchievedService(User from, List<User> to, Service service) {
        this.from = from;
        this.to = to;
        this.service = service;
        this.date = new Date();
    }

    // All @entity classes must have a public/protected no-arg constructor
    protected AchievedService() {
        this(null, Collections.emptyList(), null);
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public List<User> getTo() {
        return to;
    }

    public void setTo(List<User> to) {
        this.to = to;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public URI getPicture() {
        return picture;
    }

    public void setPicture(URI picture) {
        this.picture = picture;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
