package com.wision.demo.action;

import com.wision.demo.auth.UserContext;
import com.wision.demo.auth.UserRole;
import com.wision.demo.model.Person;
import com.wision.demo.service.DataService;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;

import javax.naming.InitialContext;
import java.io.Serializable;
import java.util.List;

@Name("personListAction")
@Scope(ScopeType.EVENT)
public class PersonListAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @In
    private FacesMessages facesMessages;

    private List<Person> persons;

    private DataService getDataService() {
        try {
            return (DataService) new InitialContext().lookup("java:module/DataService");
        } catch (Exception e) {
            throw new RuntimeException("Cannot lookup DataService", e);
        }
    }

    public void init() {
        persons = getDataService().findAllPersons();
    }

    public void deletePerson(Person person) {
        ((UserContext) Component.getInstance("userContext")).requireRole(UserRole.ADMIN);
        getDataService().deletePerson(person.getId());
        facesMessages.add("Person deleted.");
        init();
    }

    public List<Person> getPersons() {
        return persons;
    }
}
