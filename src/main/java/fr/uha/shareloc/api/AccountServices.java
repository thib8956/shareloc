package fr.uha.shareloc.api;

import fr.uha.shareloc.dao.DaoFactory;
import fr.uha.shareloc.model.Account;

import javax.ws.rs.Path;

@Path("/accounts")
public class AccountServices extends BaseServices<Account> {

    public AccountServices() {
        super(DaoFactory.createBaseDao(), Account.class);
    }

}
