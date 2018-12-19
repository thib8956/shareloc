package fr.uha.shareloc.api;

import fr.uha.shareloc.dao.BaseDao;
import fr.uha.shareloc.model.Service;

import javax.ws.rs.Path;

@Path("/services")
public class ServiceServices extends BaseServices<Service> {

    protected ServiceServices() {
        super(new BaseDao(), Service.class);
    }

}
