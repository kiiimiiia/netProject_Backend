package net.net.repository;

import net.net.entity.Paraf;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ParafRepository extends GenericRepository<Paraf,Long> {

    @Inject
    public ParafRepository (EntityManager entityManager)
    {
        super(entityManager);
    }

    public List<Paraf> getAllParafForCase(long id)
    {
        Query query =entityManager.createQuery("From Paraf where caseId=:id");
        query.setParameter("id",id);
        return query.getResultList();
    }

}
