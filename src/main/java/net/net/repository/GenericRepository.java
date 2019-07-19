package net.net.repository;


import net.net.entity.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericRepository<T extends BaseEntity, Id extends Serializable> implements BaseRepository<T, Id> {

    //    private static final Logger log = LoggerFactory.getLogger(GenericRepository.class);
    private Class<T> type;

    @PersistenceContext
    protected EntityManager entityManager;

    public GenericRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        Class genericClass = getClass();
        Type genericType = genericClass.getGenericSuperclass();
        while (!(genericType instanceof ParameterizedType)) {
            genericClass = genericClass.getSuperclass();
            if (genericClass == null) {
                throw new RuntimeException("class is not generic");
            }
            genericType = genericClass.getGenericSuperclass();
        }
        this.type = (Class<T>) ((ParameterizedType) (genericType)).getActualTypeArguments()[0];
    }

    @Override
    public T save(T entity) {
        entity = doPersist(entity);
        entityManager.flush();
        return entity;
    }

    @Override
    public List<T> save(List<T> entities) {
        List<T> mergedEntities = new ArrayList<T>();
        for (T t : entities) {
            mergedEntities.add(doMerge(t));
            entityManager.flush();
        }
        return mergedEntities;
    }

    @Override
    public List<T> save(List<T> entities, int batchSize) {
        List<T> mergedEntities = new ArrayList<>();
        int i = 0;
        for (T t : entities) {
            mergedEntities.add(doMerge(t));
            i++;
            if (i % batchSize == 0) {
                entityManager.flush();
            }
        }
        entityManager.flush();
        return mergedEntities;
    }

    @Override
    public T saveOrUpdate(T entity) {
        if (entity != null) {
            T e = entity;
            T attachedEntity;
            if (e.getId() == 0) {
                entity = doPersist(entity);
                attachedEntity = entity;
            } else {
                attachedEntity = doMerge(entity);
            }
            entityManager.flush();
            return attachedEntity;
        } else {
            throw new RuntimeException("entity is not instance of KBaseEntity");
        }
    }

    @Override
    public T edit(T entity) {
        entity = doMerge(entity);
        entityManager.flush();
        return entity;
    }

    @Override
    public void delete(Id id) {
        String query = "delete from " + type.getSimpleName() + " e where e.id = ?";
        //remove cached objects from persistence context.
        entityManager.flush();
        entityManager.clear();
        entityManager.createQuery(query).setParameter(0, id).executeUpdate();
    }

    @Override
    public void deleteIds(List<Id> ids) {
        String queryStr = " delete from " + type.getSimpleName() + " e where e.id in (:idList) ";
        Query query = entityManager.createQuery(queryStr);
        query.setParameter("idList", ids);
        query.executeUpdate();
    }

    @Override
    public void delete(T entity) {
        if (!entityManager.contains(entity)) {
            entity = doMerge(entity);
        }
        try {
            doDelete(entity);
        } catch (Exception e) {
//            log.error(e.getMessage());
        }
    }

    @Override
    public void delete(List<T> entities) {
        if (entities == null) {
            return;
        }
        int i = 0;
        try {
            for (T entry : entities) {
                doDelete(entry);
                i++;
            }
        } catch (Exception e) {
//            log.error("can not delete entity : {}", entities.get(i));
        }
    }

    @Override
    public void deleteAll() {
        String queryStr = " delete from " + type.getSimpleName() + " e ";
        Query query = entityManager.createQuery(queryStr);
        query.executeUpdate();
    }

    @Override
    public T get(Id id) {
        return entityManager.find(type, id);
    }

    @Override
    public List<T> getAll() {
        //noinspection unchecked
        return entityManager.createQuery("select o from " + type.getName() + " o").getResultList();
    }

    @Override
    public List<T> getAll(int offset, int length) {
        String queryStr = " select e from " + type.getSimpleName() + " e ";
        Query query = entityManager.createQuery(queryStr);
        query.setFirstResult(offset);
        query.setMaxResults(length);
        //noinspection unchecked
        return query.getResultList();
    }

    private T doPersist(T entity) {
        try {
            entityManager.persist(entity);
            return entity;
        } catch (javax.persistence.EntityExistsException e) {
            throw new RuntimeException("entity '" + entity.getClass().getName() + "' already persisted: "
                    + entity.toString());
        }
    }

    private T doMerge(T entity) {
        return entityManager.merge(entity);
    }

    private void doDelete(T entity) throws Exception {
        if (!entityManager.contains(entity)) {
            entity = entityManager.merge(entity);
        }
        entityManager.remove(entity);
    }

}
