package org.booklibrary.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Generic repository implementation that talks to the database via JPA,
 * mirroring the API and behaviour of {@link HashMapRepository}.
 *
 * @param <T>  aggregate root type
 * @param <ID> identifier type
 */
@NoRepositoryBean
@Transactional(readOnly = true)
public abstract class JpaEntityRepository<T, ID> implements CrudRepository<T, ID> {

    @PersistenceContext
    private EntityManager em;

    private final Class<T> domainClass;

    protected JpaEntityRepository(Class<T> domainClass) {
        this.domainClass = domainClass;
    }

    /** Implementors must expose the identifier of a managed entity. */
    protected abstract <S extends T> ID getEntityId(S entity);

    /* ---------- WRITE OPERATIONS -------------------------------------------------- */

    @Override
    @Transactional
    public <S extends T> S save(S entity) {
        Assert.notNull(entity, "entity cannot be null");
        if (getEntityId(entity) == null) {
            em.persist(entity);
            return entity;
        }
        return em.merge(entity);
    }

    @Override
    @Transactional
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        Assert.notNull(entities, "entities cannot be null");
        List<S> result = new ArrayList<>();
        entities.forEach(e -> result.add(save(e)));
        return result;
    }

    @Override
    @Transactional
    public void delete(T entity) {
        Assert.notNull(entity, "entity cannot be null");
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    @Override
    @Transactional
    public void deleteAll(Iterable<? extends T> entitiesToDelete) {
        Assert.notNull(entitiesToDelete, "entities cannot be null");
        entitiesToDelete.forEach(this::delete);
    }

    @Override
    @Transactional
    public void deleteAll() {
        String jpql = "DELETE FROM " + domainClass.getName() + " e";
        em.createQuery(jpql).executeUpdate();
    }

    @Override
    @Transactional
    public void deleteById(ID id) {
        Assert.notNull(id, "Id cannot be null");
        findById(id).ifPresent(this::delete);
    }

    @Override
    public Optional<T> findById(ID id) {
        Assert.notNull(id, "Id cannot be null");
        return Optional.ofNullable(em.find(domainClass, id));
    }

    public T findOne(ID id) {   // convenience, like HashMapRepository
        return findById(id).orElse(null);
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        Assert.notNull(ids, "Ids cannot be null");
        List<T> result = new ArrayList<>();
        ids.forEach(id -> findById(id).ifPresent(result::add));
        return result;
    }

    @Override
    public Collection<T> findAll() {
        TypedQuery<T> query =
                em.createQuery("FROM " + domainClass.getName(), domainClass);
        return query.getResultList();
    }

    @Override
    public long count() {
        String jpql = "SELECT COUNT(e) FROM " + domainClass.getName() + " e";
        return em.createQuery(jpql, Long.class).getSingleResult();
    }

    @Override
    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }
}