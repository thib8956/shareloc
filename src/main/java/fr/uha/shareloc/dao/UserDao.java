package fr.uha.shareloc.dao;

import fr.uha.shareloc.model.User;

public class UserDao extends AbstractDao<User> {
    public UserDao() {
        super(User.class);
    }

}
