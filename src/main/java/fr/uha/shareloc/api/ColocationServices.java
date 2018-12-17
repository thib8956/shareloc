package fr.uha.shareloc.api;

import fr.uha.shareloc.dao.AbstractDao;
import fr.uha.shareloc.dao.AccountDao;
import fr.uha.shareloc.model.AchievedService;
import fr.uha.shareloc.model.Colocation;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/colocations")
public class ColocationServices extends BaseServices<Colocation> {

    private final AccountDao dao;

    protected ColocationServices() {
        super(new AbstractDao<>(Colocation.class));
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

}
