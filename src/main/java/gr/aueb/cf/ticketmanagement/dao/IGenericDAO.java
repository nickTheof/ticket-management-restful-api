package gr.aueb.cf.ticketmanagement.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IGenericDAO<T> {
    Optional<T> insert(T t);
    Optional<T> update(T t);
    void delete(Object id);
    Optional<T> getById(Object id);
    Optional<T> findByField(String field, Object value);
    List<T> getAll();
    List<T> getByCriteria(Map<String, Object> criteria);
    <K extends T> List<K> getByCriteria(Class<K> clazz, Map<String, Object> criteria);
    Long count();
    Long countByCriteria(Map<String, Object> criteria);
    <K extends T> List<K> getByCriteriaPaginated(Class<K> clazz, Map<String, Object> criteria, Integer page, Integer size);
}
