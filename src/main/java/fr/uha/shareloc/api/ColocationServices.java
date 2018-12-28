package fr.uha.shareloc.api;

import fr.uha.shareloc.dao.ColocationDao;
import fr.uha.shareloc.model.Colocation;
import fr.uha.shareloc.model.Service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/colocations")
public class ColocationServices extends BaseServices<Colocation> {

    private final ColocationDao dao;

    protected ColocationServices() {
        super(new ColocationDao(), Colocation.class);
        this.dao = (ColocationDao) getDao();
    }

    @POST
    @Path("invite")
    public Response inviteUser(@QueryParam("login") String login, @QueryParam("colocId") int colocId) {
        if (dao.inviteUser(colocId, login)) return Response.status(Response.Status.CREATED).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("offer")
    public Response offer(@QueryParam("colocationId") int colocationId, @QueryParam("serviceId") int serviceId) {
        final ColocationDao dao = (ColocationDao) getDao();
        final Colocation c = dao.find(colocationId, Colocation.class);
        final Service s = dao.findService(serviceId);
        if (c == null || s == null) return Response.status(Response.Status.NOT_FOUND).build();
        c.addService(s);
        getDao().update(c);
        return Response.ok().build();
    }

}
