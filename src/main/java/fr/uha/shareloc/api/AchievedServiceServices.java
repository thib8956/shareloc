package fr.uha.shareloc.api;

import fr.uha.shareloc.dao.BaseDao;
import fr.uha.shareloc.model.AchievedService;

import javax.ws.rs.Path;

@Path("/achieved")
public class AchievedServiceServices extends BaseServices<AchievedService> {

    protected AchievedServiceServices() {
        super(new BaseDao(), AchievedService.class);
    }

}
