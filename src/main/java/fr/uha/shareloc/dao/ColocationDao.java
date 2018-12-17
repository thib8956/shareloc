package fr.uha.shareloc.dao;

import fr.uha.shareloc.model.Colocation;
import fr.uha.shareloc.model.Service;

public class ColocationDao extends AbstractDao<Colocation> {

    public ColocationDao() {
        super(Colocation.class);
    }

    public Service findService(Integer serviceId) {
        return getEntityManager().find(Service.class, serviceId);
    }
}
