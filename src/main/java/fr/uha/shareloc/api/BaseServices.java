package fr.uha.shareloc.api;

import fr.uha.shareloc.dao.AbstractDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class BaseServices<T> {

    private final AbstractDao<T> dao;

    BaseServices(Class<T> serviceClass) {
        this.dao = new AbstractDao<>(serviceClass);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok()
                .entity(dao.findAll())
                .build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Integer id) {
        final T obj = dao.find(id);
        if (obj == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok()
                .entity(obj)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(T resource) {
        // TODO: error checking & management
        dao.create(resource);
        return Response.status(Response.Status.CREATED)
                .entity("Saved : " + resource)
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Integer id) {
        final T obj = dao.find(id);
        if (obj == null) return Response.status(Response.Status.NOT_FOUND).build();
        dao.remove(obj);
        return Response.ok().build();
    }

    @PUT
    @Path("{id}")
    public Response edit(@PathParam("id") Integer id) {
        final T obj = dao.find(id);
        if (obj == null) return Response.status(Response.Status.NOT_FOUND).build();
        dao.edit(obj);
        return Response.status(Response.Status.CREATED)
                .entity("Updated : " + obj)
                .build();
    }
}
