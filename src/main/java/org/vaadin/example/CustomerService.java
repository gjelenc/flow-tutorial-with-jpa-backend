package org.vaadin.example;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Minimally transitioned the example dummy service into EJB accessing database.
 */
@Stateless
public class CustomerService {

    @PersistenceContext
    EntityManager entityManager;

    private static final Logger LOGGER = Logger.getLogger(CustomerService.class.getName());

    /**
     * @return all available Customer objects.
     */
    public synchronized List<Customer> findAll() {
        return findAll(null);
    }

    /**
     * Finds all Customer's that match given filter.
     *
     * @param stringFilter filter that returned objects should match or
     * null/empty string if all objects should be returned.
     * @return list a Customer objects
     */
    public synchronized List<Customer> findAll(String stringFilter) {
        Query query;
        if(stringFilter == null) {
            query = entityManager.createQuery("SELECT c FROM Customer c");
        } else {
            query = entityManager.createQuery("SELECT c FROM Customer c WHERE LOWER(CONCAT(c.firstName, c.lastName)) LIKE :filter", Customer.class).setParameter("filter", "%" + stringFilter.toLowerCase() + "%");
        }
        return query.getResultList();
    }

    /**
     * Deletes a customer from a system
     *
     * @param value the Customer to be deleted
     */
    public synchronized void delete(Customer value) {
        entityManager.remove(entityManager.merge(value));
    }

    /**
     * Persists or updates customer in the system. Also assigns an identifier
     * for new Customer instances.
     *
     * @param entry
     */
    public synchronized void save(Customer entry) {
        if (entry == null) {
            LOGGER.log(Level.SEVERE,
                    "Customer is null. Are you sure you have connected your form to the application as described in tutorial chapter 7?");
            return;
        }
        if (entry.getId() == null) {
            entityManager.persist(entry);
        } else {
            entityManager.merge(entry);
        }
    }

    /**
     * Sample data generation
     */
    @PostConstruct
    public void ensureTestData() {
        if (findAll().isEmpty()) {
            final String[] names = new String[]{"Gabrielle Patel", "Brian Robinson", "Eduardo Haugen",
                "Koen Johansen", "Alejandro Macdonald", "Angel Karlsson", "Yahir Gustavsson", "Haiden Svensson",
                "Emily Stewart", "Corinne Davis", "Ryann Davis", "Yurem Jackson", "Kelly Gustavsson",
                "Eileen Walker", "Katelyn Martin", "Israel Carlsson", "Quinn Hansson", "Makena Smith",
                "Danielle Watson", "Leland Harris", "Gunner Karlsen", "Jamar Olsson", "Lara Martin",
                "Ann Andersson", "Remington Andersson", "Rene Carlsson", "Elvis Olsen", "Solomon Olsen",
                "Jaydan Jackson", "Bernard Nilsen"};
            Random r = new Random(0);
            for (String name : names) {
                String[] split = name.split(" ");
                Customer c = new Customer();
                c.setFirstName(split[0]);
                c.setLastName(split[1]);
                c.setStatus(CustomerStatus.values()[r.nextInt(CustomerStatus.values().length)]);
                save(c);
            }
        }
    }
}
