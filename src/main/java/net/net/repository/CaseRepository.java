package net.net.repository;


import net.net.entity.Cases;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class CaseRepository extends GenericRepository<Cases, Long> {
    @Inject
    public CaseRepository(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Cases> getCasesByUser (long id)
    {
        Query query = entityManager.createQuery("From Cases where senderId=:id");
        query.setParameter("id",id);
        return query.getResultList();
    }

    public List<Cases> getReferCase (long id)
    {
        Query query = entityManager.createQuery("From Cases where receiverId=:id order by id");
        query.setParameter("id",id);
        return query.getResultList();
    }

    public int getCasesForUser (long id)
    {
        Query query = entityManager.createQuery("From Cases where receiverId=:id");
        query.setParameter("id",id);
        return query.getResultList().size();
    }
}
