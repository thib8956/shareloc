package fr.uha.shareloc.util;

import javax.persistence.TypedQuery;
import java.util.List;

public final class JPAHelper {
    public static <T> T getSingleResultOrNull(TypedQuery<T> query) {
        query.setMaxResults(1);
        List<T> list = query.getResultList();
        return list == null || list.isEmpty() ? null : list.get(0);
    }
}
