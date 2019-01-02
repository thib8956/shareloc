package fr.uha.shareloc.api;

import fr.uha.shareloc.dao.DaoFactory;
import fr.uha.shareloc.model.AchievedService;

import javax.ws.rs.Path;

@Path("/achieved")
public class AchievedServiceServices extends BaseServices<AchievedService> {

    protected AchievedServiceServices() {
        super(DaoFactory.createBaseDao(), AchievedService.class);
    }

}
