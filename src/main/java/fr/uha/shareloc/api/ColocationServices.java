package fr.uha.shareloc.api;

import fr.uha.shareloc.dao.ColocationDao;
import fr.uha.shareloc.model.Colocation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/colocations")
public class ColocationServices {

    private final ColocationDao dao;

    public ColocationServices() {
        this.dao = new ColocationDao();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getColocations() {
        return Response.ok()
                .entity(dao.findAll())
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addColocation(Colocation colocation) {
        // TODO: error checking & management
        dao.create(colocation);
        return Response.status(Response.Status.CREATED)
                .entity("Saved : " + colocation)
                .build();
    }
}
