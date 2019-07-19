package net.net.repository;


import net.net.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @author Rouzbeh Karimi
 * @since 5/3/2017
 */
public interface BaseRepository<T extends BaseEntity, Id extends Serializable> {

    T save(T entity);

    List<T> save(List<T> entities);

    List<T> save(List<T> entities, int batchSize);

    T saveOrUpdate(T entity);

    T edit(T entity);

    void delete(Id id);

    void deleteIds(List<Id> ids);

    void delete(T entity);

    void delete(List<T> entities);

    void deleteAll();

    T get(Id id);

    List<T> getAll();

    List<T> getAll(int offset, int length);

}

