package fr.uha.shareloc.api;

import fr.uha.shareloc.model.Colocation;

import javax.ws.rs.Path;

@Path("/colocations")
public class ColocationServices extends BaseServices<Colocation> {

    protected ColocationServices() {
        super(Colocation.class);
    }

}
