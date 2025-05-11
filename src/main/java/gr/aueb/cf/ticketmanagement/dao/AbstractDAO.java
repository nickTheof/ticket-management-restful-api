package gr.aueb.cf.ticketmanagement.dao;

import gr.aueb.cf.ticketmanagement.model.IdentifiableEntity;
import gr.aueb.cf.ticketmanagement.service.util.JPAHelper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.util.*;

public abstract class AbstractDAO<T extends IdentifiableEntity> implements IGenericDAO<T> {
    private Class<T> persistentClass;

    public AbstractDAO() {

    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    public void setPersistentClass(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }


    @Override
    public Optional<T> insert(T t) {
        EntityManager em = getEntityManager();
        em.persist(t);
        return Optional.of(t);
    }

    @Override
    public Optional<T> update(T t) {
        Optional<T> toUpdate = getById(t.getId());
        if (toUpdate.isPresent()) {
            getEntityManager().merge(t);
            return Optional.of(t);
        }
        return Optional.empty();
    }

    @Override
    public void delete(Object id) {
        Optional<T> toDelete = getById(id);
        toDelete.ifPresent(getEntityManager()::remove);
    }

    @Override
    public Optional<T> getById(Object id) {
        EntityManager em = getEntityManager();
        return Optional.ofNullable(em.find(persistentClass, id));
    }

    @Override
    public Optional<T> findByField(String field, Object value) {
        String sql = "SELECT e FROM " + getPersistentClass().getSimpleName() + " e WHERE e." + field + " :=value";
        TypedQuery<T> query = getEntityManager().createQuery(sql, persistentClass);
        query.setParameter("value", value);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<T> getAll() {
        return getByCriteria(Collections.emptyMap());
    }

    @Override
    public List<T> getByCriteria(Map<String, Object> criteria) {
        return getByCriteria(persistentClass, criteria);
    }

    @Override
    public <K extends T> List<K> getByCriteria(Class<K> clazz, Map<String, Object> criteria) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<K> selectQuery = builder.createQuery(clazz);
        Root<K> entityRoot = selectQuery.from(clazz);
        List<Predicate> predicates = getPredicates(builder, entityRoot, criteria);
        selectQuery.select(entityRoot).where(predicates.toArray(new Predicate[0]));
        TypedQuery<K> query = getEntityManager().createQuery(selectQuery);
        addParametersToQuery(query, criteria);
        return query.getResultList();
    }

    @Override
    public Long count() {
        String sql = "SELECT COUNT(e) FROM " + getPersistentClass().getSimpleName() + " e";
        return getEntityManager().createQuery(sql, Long.class).getSingleResult();
    }

    @Override
    public Long countByCriteria(Map<String, Object> criteria) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<T> entityRoot = countQuery.from(persistentClass);
        List<Predicate> predicates = getPredicates(builder, entityRoot, criteria);
        countQuery.select(builder.count(entityRoot)).where(predicates.toArray(new Predicate[0]));
        TypedQuery<Long> query = getEntityManager().createQuery(countQuery);
        addParametersToQuery(query, criteria);
        return query.getSingleResult();
    }

    @Override
    public <K extends T> List<K> getByCriteriaPaginated(Class<K> clazz, Map<String, Object> criteria, Integer page, Integer size) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<K> selectQuery = builder.createQuery(clazz);
        Root<K> entityRoot = selectQuery.from(clazz);
        List<Predicate> predicates = getPredicates(builder, entityRoot, criteria);
        selectQuery.select(entityRoot).where(predicates.toArray(new Predicate[0]));
        TypedQuery<K> query = getEntityManager().createQuery(selectQuery);
        addParametersToQuery(query, criteria);
        if (page != null && size != null) {
            query.setFirstResult(page * size);
            query.setMaxResults(size);
        }

        return query.getResultList();
    }

    protected EntityManager getEntityManager() {
        return JPAHelper.getEntityManager();
    }

    protected String buildParameterAlias(String alias) {
        return alias.replaceAll("\\.", "");
    }

    protected Path<?> resolvePath(Root<? extends T> root, String expression) {
        String[] fields = expression.split("\\.");
        Path<?> path = root.get(fields[0]);
        for (int i=1; i < fields.length; i++) {
            path = path.get(fields[i]);
        }
        return path;
    }

    protected void addParametersToQuery(TypedQuery<?> query, Map<String, Object> criteria) {
        for (Map.Entry<String, Object> entry: criteria.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();
            if (val instanceof List || val instanceof Map) {
                // Handle complex cases like IN or BETWEEN that need special parameter setting
                // (    Do not add % for LIKE here)
                query.setParameter(buildParameterAlias(key), val);
            } else {
                // Adding '%' for LIKE operations if needed
                query.setParameter(buildParameterAlias(key), val + "%");
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected List<Predicate> getPredicates(CriteriaBuilder builder, Root<? extends T> entityRoot, Map<String, Object> criteria) {
        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, Object> entry: criteria.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();

            // Handling the cases where the value is a List, Map or a "isNull" condition
            if (val instanceof List) {
                Path<?> path = resolvePath(entityRoot, key);
                CriteriaBuilder.In<Object> inClause = builder.in(path);
                for (Object v : (List<?>) val) {
                    inClause.value(v);
                }
                predicates.add(inClause);
            } else if (val instanceof Map) {
                // For 'BETWEEN' condition
                Map<String, Object> mapValue = (Map<String, Object>) val;
                if (mapValue.containsKey("from") && (mapValue.containsKey("to"))) {
                    Object from = mapValue.get("from");
                    Object to = mapValue.get("to");

                    if (from instanceof Comparable && to instanceof Comparable) {
                        Expression<? extends Comparable<Object>> path = (Expression<? extends Comparable<Object>>) resolvePath(entityRoot, key);
                        predicates.add(builder.between(path, (Comparable<Object>) from, (Comparable<Object>) to));
                    }
                }
            } else if ("isNull".equals(val)) {
                predicates.add(builder.isNull(resolvePath(entityRoot, key)));
            } else if ("isNotNull".equals(val)) {
                predicates.add(builder.isNotNull(resolvePath(entityRoot, key)));
            } else if (val instanceof String && ((String) val).contains("%")) {
                // Treat as LIKE pattern (e.g., "Jo%")
                predicates.add(
                        builder.like(
                                builder.lower((Expression<String>) resolvePath(entityRoot, key)),
                                ((String) val).toLowerCase()
                        ));
            } else {
                // For '=' condition (default case)
                predicates.add(builder.equal(resolvePath(entityRoot, key), val));
            }
        }
        return predicates;
    }
}
