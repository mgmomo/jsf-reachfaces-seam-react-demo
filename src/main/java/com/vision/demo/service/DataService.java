package com.vision.demo.service;

import com.vision.demo.model.Location;
import com.vision.demo.model.LocationState;
import com.vision.demo.model.Person;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class DataService {

    @PersistenceContext(unitName = "vision4Pu")
    private EntityManager em;

    // Person operations

    @SuppressWarnings("unchecked")
    public List<Person> findAllPersons() {
        return em.createQuery("SELECT DISTINCT p FROM Person p LEFT JOIN FETCH p.locations ORDER BY p.lastName, p.firstName")
                 .getResultList();
    }

    public Person findPerson(Long id) {
        List<Person> results = em.createQuery("SELECT p FROM Person p LEFT JOIN FETCH p.locations WHERE p.id = :id")
                                  .setParameter("id", id)
                                  .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public Person savePerson(Person person) {
        // Re-fetch detached locations as managed entities so the
        // join table is correctly updated (no cascade on @ManyToMany)
        List<Location> managedLocations = new ArrayList<Location>();
        for (Location loc : person.getLocations()) {
            if (loc.getId() != null) {
                Location managed = em.find(Location.class, loc.getId());
                if (managed != null) {
                    managedLocations.add(managed);
                }
            }
        }

        Person managed;
        if (person.getId() == null) {
            person.getLocations().clear();
            em.persist(person);
            managed = person;
        } else {
            managed = em.merge(person);
            managed.getLocations().clear();
        }
        for (Location loc : managedLocations) {
            managed.getLocations().add(loc);
        }
        return managed;
    }

    public void deletePerson(Long personId) {
        Person person = em.find(Person.class, personId);
        if (person != null) {
            person.getLocations().clear();
            em.remove(person);
        }
    }

    // Location operations

    @SuppressWarnings("unchecked")
    public List<Location> findAllLocations() {
        return em.createQuery("SELECT l FROM Location l ORDER BY l.locationName")
                 .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Location> findActiveLocations() {
        return em.createQuery("SELECT l FROM Location l WHERE l.state = :state ORDER BY l.locationName")
                 .setParameter("state", LocationState.ACTIVE)
                 .getResultList();
    }

    public Location findLocation(Long id) {
        return em.find(Location.class, id);
    }

    public Location saveLocation(Location location) {
        if (location.getId() == null) {
            em.persist(location);
            return location;
        } else {
            return em.merge(location);
        }
    }

    @SuppressWarnings("unchecked")
    public void deleteLocation(Long locationId) {
        Location location = em.find(Location.class, locationId);
        if (location != null) {
            List<Person> persons = em.createQuery(
                    "SELECT p FROM Person p JOIN p.locations l WHERE l.id = :locId")
                    .setParameter("locId", locationId)
                    .getResultList();
            for (Person p : persons) {
                p.getLocations().remove(location);
            }
            em.remove(location);
        }
    }
}
