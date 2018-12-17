package fr.uha.shareloc.api;

import fr.uha.shareloc.dao.AbstractDao;
import fr.uha.shareloc.model.User;

import javax.ws.rs.Path;

@Path("/users")
public class UserServices extends BaseServices<User> {

    protected UserServices() {
        super(new AbstractDao<>(User.class));
    }

}
