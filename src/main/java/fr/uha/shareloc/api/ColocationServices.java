package fr.uha.shareloc.api;

import fr.uha.shareloc.dao.AbstractDao;
import fr.uha.shareloc.dao.AccountDao;
import fr.uha.shareloc.dao.ColocationDao;
import fr.uha.shareloc.model.AchievedService;
import fr.uha.shareloc.model.Colocation;
import fr.uha.shareloc.model.Service;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/colocations")
public class ColocationServices extends BaseServices<Colocation> {

    private final AccountDao dao;

    protected ColocationServices() {
        super(new ColocationDao());
        this.dao = new AccountDao();
    }

    @POST
    @Path("invite")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response inviteUser(JsonObject jsonObject) {
        final int userId = jsonObject.getInt("userId");
        final int colocId = jsonObject.getInt("colocId");
        if (dao.inviteUser(colocId, userId)) return Response.status(Response.Status.CREATED).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("{id}/offer/{serviceId}")
    public Response offer(@PathParam("id") Integer id, @PathParam("serviceId") Integer serviceId) {
        final ColocationDao dao = (ColocationDao) getDao();
        final Colocation c = dao.find(id);
        final Service s = dao.findService(serviceId);
        if (c == null || s == null) return Response.status(Response.Status.NOT_FOUND).build();
        c.addService(s);
        getDao().edit(c);
        return Response.ok().build();
    }

}
