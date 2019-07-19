package net.net.repository;

import net.net.entity.User;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserRepository extends GenericRepository<User, Long> {


    @Inject
    public UserRepository(EntityManager entityManager) {
        super(entityManager);
    }

    public User FindByUserNameAndPass(String userName, String passWord) {
        Query query = entityManager.createQuery("From User where userName=:userName and passWord=:passWord ");
        query.setParameter("userName", userName);
        query.setParameter("passWord", passWord);
        return query.getResultList().size() > 0 ? (User) query.getResultList().get(0) : null;
    }

    public List<User> getUnConfirmedUser() {
        Query query = entityManager.createQuery("From User where confirmed=:confirmed order by id");
        query.setParameter("confirmed", 0);
        return query.getResultList();
    }

    public User getUserByToken(String token) {
        Query query = entityManager.createQuery("From User where token=:token");
        query.setParameter("token", token);
        return query.getResultList().size() > 0 ? (User) query.getResultList().get(0) : null;
    }



    public boolean checkAvailableAccountInformation(String phone,String Email) {

        Query query = entityManager.createQuery("From User where phoneNumber=:phone or mailAddress=:Email");
        query.setParameter("phone",phone);
        query.setParameter("Email",Email);
        if (query.getResultList().size() == 0)
            return true;
        return false;
    }

}
