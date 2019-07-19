package net.net.repository;

import net.net.entity.Role;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Repository
public class RoleRepository extends GenericRepository<Role,Long> {

    @Inject
    public RoleRepository (EntityManager entityManager)
    {
        super(entityManager);
    }

    public Role getRole (String roleName)
    {
        System.out.println(roleName);
        Query query = entityManager.createQuery("From Role where roleName=:roleName");
        query.setParameter("roleName",roleName);
        return (Role) query.getResultList().get(0);
    }
}
