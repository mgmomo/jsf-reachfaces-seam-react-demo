package com.vision.demo.action;

import com.vision.demo.auth.UserContext;
import com.vision.demo.auth.UserRole;
import com.vision.demo.model.Location;
import com.vision.demo.model.Person;
import com.vision.demo.service.DataService;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.faces.FacesMessages;

import javax.naming.InitialContext;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Name("personAction")
@Scope(ScopeType.CONVERSATION)
public class PersonAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @In
    private FacesMessages facesMessages;

    private Long personId;
    private Person person;
    private Long selectedLocationId;

    private DataService getDataService() {
        try {
            return (DataService) new InitialContext().lookup("java:module/DataService");
        } catch (Exception e) {
            throw new RuntimeException("Cannot lookup DataService", e);
        }
    }

    private UserContext getUserContext() {
        return (UserContext) Component.getInstance("userContext");
    }

    @Begin(join = true)
    public void init() {
        if (person != null) {
            return; // Already initialized in this conversation
        }
        if (personId != null && personId > 0) {
            person = getDataService().findPerson(personId);
            if (person == null) {
                facesMessages.add("Person not found.");
                person = new Person();
            }
        } else {
            person = new Person();
        }
    }

    @End
    public String save() {
        getUserContext().requireRole(UserRole.ADMIN);
        getDataService().savePerson(person);
        facesMessages.add("Person saved successfully.");
        return "/personList.xhtml";
    }

    @End
    public String cancel() {
        return "/personList.xhtml";
    }

    public void addLocation() {
        getUserContext().requireRole(UserRole.ADMIN);
        if (selectedLocationId != null) {
            Location location = getDataService().findLocation(selectedLocationId);
            if (location != null && !person.getLocations().contains(location)) {
                person.getLocations().add(location);
            }
            selectedLocationId = null;
        }
    }

    public void removeLocation(Location location) {
        getUserContext().requireRole(UserRole.ADMIN);
        person.getLocations().remove(location);
    }

    public List<SelectItem> getAvailableLocationItems() {
        List<Location> allActive = getDataService().findActiveLocations();
        List<SelectItem> items = new ArrayList<SelectItem>();
        for (Location loc : allActive) {
            if (!person.getLocations().contains(loc)) {
                items.add(new SelectItem(loc.getId(), loc.getLocationName() + " (" + loc.getZipCode() + ")"));
            }
        }
        return items;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Long getSelectedLocationId() {
        return selectedLocationId;
    }

    public void setSelectedLocationId(Long selectedLocationId) {
        this.selectedLocationId = selectedLocationId;
    }
}
