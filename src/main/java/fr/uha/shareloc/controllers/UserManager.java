package fr.uha.shareloc.controllers;

import fr.uha.shareloc.dao.BaseDao;
import fr.uha.shareloc.dao.DaoFactory;
import fr.uha.shareloc.model.User;

public class UserManager {

    private static BaseDao dao = DaoFactory.createBaseDao();

    public static User getUser(String login) {
        if (login == null) return null;
        return dao.find(login, User.class);
    }

    public static User login(String login, String password) {
        User u = dao.find(login, User.class);
        if (u != null && u.getPassword().equals(password)) return u;
        return null;
    }

    public static boolean createUser(String login, String password, String firstname, String lastname) {
        User u = dao.find(login, User.class);
        if (u == null) {
            dao.create(new User(login, password, firstname, lastname));
            return true;
        }

        return false;
    }

}
