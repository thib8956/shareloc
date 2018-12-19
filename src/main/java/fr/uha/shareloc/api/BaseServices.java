package fr.uha.shareloc.api;

import fr.uha.shareloc.dao.BaseDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public abstract class BaseServices<T> {

    private final BaseDao dao;
    private Class<T> clazz;

    BaseServices(BaseDao dao, Class<T> clazz) {
        this.dao = dao;
        this.clazz = clazz;
    }

    protected BaseDao getDao() {
        return dao;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok()
                .entity(dao.findAll(clazz))
                .build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Integer id) {
        final T obj = dao.find(id, clazz);
        if (obj == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok()
                .entity(obj)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(T resource) {
        // TODO: error checking & management
        final T createdResource = dao.create(resource);
        return Response.status(Response.Status.CREATED)
                .entity("Saved : " + createdResource)
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Integer id) {
        final T obj = dao.find(id, clazz);
        if (obj == null) return Response.status(Response.Status.NOT_FOUND).build();
        dao.remove(obj);
        return Response.ok().build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    // FIXME
    public Response edit(@PathParam("id") Integer id, T entity) {
//        final T obj = dao.find(id, clazz);
//        if (obj == null) return Response.status(Response.Status.NOT_FOUND).build();
        final T updatedEntity = dao.update(entity);
        return Response.status(Response.Status.CREATED)
                .entity("Updated : " + updatedEntity)
                .build();
    }

    @GET
    @Path("/count")
    public Response count() {
        return Response.ok().entity(dao.count(clazz)).build();
    }
}
