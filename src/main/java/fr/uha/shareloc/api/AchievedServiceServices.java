package fr.uha.shareloc.api;

import fr.uha.shareloc.dao.AbstractDao;
import fr.uha.shareloc.model.AchievedService;

import javax.ws.rs.Path;

@Path("/achieved")
public class AchievedServiceServices extends BaseServices<AchievedService> {

    protected AchievedServiceServices() {
        super(new AbstractDao<>(AchievedService.class));
    }

}
