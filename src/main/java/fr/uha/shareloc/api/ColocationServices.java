package fr.uha.shareloc.api;

import fr.uha.shareloc.dao.ColocationDao;
import fr.uha.shareloc.model.Colocation;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/colocations")
public class ColocationServices extends BaseServices<Colocation> {

    private final ColocationDao dao;

    protected ColocationServices() {
        super(Colocation.class);
        this.dao = new ColocationDao();
    }

    @POST
    @Path("invite")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response inviteUser(JsonObject jsonObject) {
        final int userId = jsonObject.getInt("userId");
        final int colocId = jsonObject.getInt("colocId");
        if (dao.inviteUser(colocId, userId)) return Response.ok().build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
