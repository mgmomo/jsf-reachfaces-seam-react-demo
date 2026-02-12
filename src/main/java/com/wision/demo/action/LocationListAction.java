package com.wision.demo.action;

import com.wision.demo.auth.UserContext;
import com.wision.demo.auth.UserRole;
import com.wision.demo.model.Location;
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

@Name("locationListAction")
@Scope(ScopeType.EVENT)
public class LocationListAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @In
    private FacesMessages facesMessages;

    private List<Location> locations;

    private DataService getDataService() {
        try {
            return (DataService) new InitialContext().lookup("java:module/DataService");
        } catch (Exception e) {
            throw new RuntimeException("Cannot lookup DataService", e);
        }
    }

    public void init() {
        locations = getDataService().findAllLocations();
    }

    public void deleteLocation(Location location) {
        ((UserContext) Component.getInstance("userContext")).requireRole(UserRole.ADMIN);
        getDataService().deleteLocation(location.getId());
        facesMessages.add("Location deleted.");
        init();
    }

    public List<Location> getLocations() {
        return locations;
    }
}
