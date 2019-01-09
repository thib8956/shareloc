package fr.uha.shareloc.api;

import fr.uha.shareloc.dao.ColocationDao;
import fr.uha.shareloc.dao.DaoFactory;
import fr.uha.shareloc.model.Colocation;
import fr.uha.shareloc.model.Service;
import fr.uha.shareloc.model.User;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/colocations")
public class ColocationServices extends BaseServices<Colocation> {

    private final ColocationDao dao;

    protected ColocationServices() {
        super(DaoFactory.createColocationDao(), Colocation.class);
        this.dao = (ColocationDao) getDao();
    }

    @POST
    @Path("invite")
    public Response inviteUser(@FormParam("login") String login, @FormParam("colocId") int colocId) {
        if (dao.inviteUser(colocId, login)) return Response.status(Response.Status.CREATED).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("offer")
    public Response offer(@FormParam("colocationId") int colocationId, @FormParam("serviceId") int serviceId) {
        final ColocationDao dao = (ColocationDao) getDao();
        final Colocation c = dao.find(colocationId, Colocation.class);
        final Service s = dao.find(serviceId);
        if (c == null || s == null) return Response.status(Response.Status.NOT_FOUND).build();
        c.addService(s);
        getDao().update(c);
        return Response.ok().build();
    }

    @POST
    @Path("create")
    public Response create(@FormParam("name") String name, @FormParam("admin") String adminLogin) {
        final ColocationDao dao = (ColocationDao) getDao();
        if (!dao.createColocation(adminLogin, name)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(adminLogin)
                    .build();
        }
        return Response.ok().build();
    }

}
