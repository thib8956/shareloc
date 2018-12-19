package fr.uha.shareloc.api;

import fr.uha.shareloc.dao.BaseDao;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class BaseServicesTest {

    private BaseDao dao;
    private BaseServices<Object> services;

    @Before
    public void setUp() {
        dao = mock(BaseDao.class);
        services = new BaseServices<Object>(dao, Object.class) {
        };
    }

    @Test
    public void testCreate() {
        final Object entity = new Object();
        when(dao.create(entity)).thenReturn(entity);
        final Response response = services.create(entity);
        verify(dao).create(entity);
        final int statusCode = Response.Status.CREATED.getStatusCode();
        assertThat(response.getStatus(), is(statusCode));
        assertThat(response.getEntity(), is("Saved : " + entity));
    }

    @Test
    public void testGetAll_empty() {
        when(dao.findAll(Object.class)).thenReturn(Collections.emptyList());
        final Response response = services.getAll();
        verify(dao).findAll(Object.class);
        final int statusCode = Response.Status.OK.getStatusCode();
        assertThat(response.getStatus(), is(statusCode));
        assertThat(response.getEntity(), is(Collections.emptyList()));
    }

    @Test
    public void testGetAll_single() {
        final Object entity = new Object();
        when(dao.findAll(Object.class)).thenReturn(Collections.singletonList(entity));
        final Response response = services.getAll();
        verify(dao).findAll(Object.class);
        final int statusCode = Response.Status.OK.getStatusCode();
        assertThat(response.getStatus(), is(statusCode));
        assertThat(response.getEntity(), is(Collections.singletonList(entity)));
    }

    @Test
    public void testGet() {
        final Object entity = new Object();
        final int id = 42;
        when(dao.find(id, Object.class)).thenReturn(entity);
        final Response response = services.get(id);
        verify(dao).find(id, Object.class);
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertThat(response.getEntity(), is(entity));
    }

    @Test
    public void testGet_notfound() {
        final int id = 42;
        when(dao.find(id, Object.class)).thenReturn(null);
        final Response response = services.get(id);
        verify(dao).find(id, Object.class);
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void testDelete_notfound() {
        final int id = 42;
        when(dao.find(id, Object.class)).thenReturn(null);
        final Response response = services.delete(id);
        verify(dao).find(id, Object.class);
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void testDelete() {
        final Object entity = new Object();
        final int id = 42;
        when(dao.find(id, Object.class)).thenReturn(entity);
        final Response response = services.delete(id);
        verify(dao).find(id, Object.class);
        verify(dao).remove(entity);
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void testEdit() {
        final Object entity = new Object();
        when(dao.update(entity)).thenReturn(entity);
        final Response response = services.edit(42, entity);
        verify(dao).update(entity);
        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));
        assertThat(response.getEntity(), is("Updated : " + entity));
    }
}