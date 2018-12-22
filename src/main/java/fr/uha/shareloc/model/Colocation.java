package fr.uha.shareloc.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Colocation implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    @OneToOne private User admin;
    @OneToMany private List<Service> services;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public void addService(Service service) {
        services.add(service);
    }
}
