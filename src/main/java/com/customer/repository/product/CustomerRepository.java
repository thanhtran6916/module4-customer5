package com.customer.repository.product;

import com.customer.model.Customer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class CustomerRepository implements ICustomerRepository {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Customer> getAll() {
        TypedQuery<Customer> query = entityManager.createQuery("from Customer", Customer.class);
        return query.getResultList();
    }

    @Override
    public void save(Customer customer) {
        if (customer.getId() == null) {
            entityManager.persist(customer);
        } else {
            entityManager.merge(customer);
        }
    }

    @Override
    public void delete(Long id) {
        Customer customer = entityManager.find(Customer.class, id);
        entityManager.remove(customer);
    }

    @Override
    public Customer getById(Long id) {
        return entityManager.find(Customer.class, id);
    }

    @Override
    public List<Customer> getByName(String name) {
        TypedQuery<Customer> query = entityManager.createQuery("select c from Customer as c where c.name like ?1", Customer.class);
        query.setParameter(1, name);
        return query.getResultList();
    }
}
