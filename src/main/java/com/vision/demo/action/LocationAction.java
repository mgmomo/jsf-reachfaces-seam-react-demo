package com.vision.demo.action;

import com.vision.demo.auth.UserContext;
import com.vision.demo.auth.UserRole;
import com.vision.demo.model.Location;
import com.vision.demo.model.LocationState;
import com.vision.demo.service.DataService;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.faces.FacesMessages;

import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Name("locationAction")
@Scope(ScopeType.CONVERSATION)
public class LocationAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @In
    private FacesMessages facesMessages;

    private Long locationId;
    private Location location;

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
        if (location != null) {
            return; // Already initialized in this conversation
        }
        if (locationId != null && locationId > 0) {
            location = getDataService().findLocation(locationId);
            if (location == null) {
                facesMessages.add("Location not found.");
                location = new Location();
            }
        } else {
            location = new Location();
        }
    }

    @End
    public String save() {
        getUserContext().requireRole(UserRole.ADMIN);
        getDataService().saveLocation(location);
        facesMessages.add("Location saved successfully.");
        return "/locationList.xhtml";
    }

    @End
    public String cancel() {
        return "/locationList.xhtml";
    }

    public List<SelectItem> getStateItems() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        for (LocationState s : LocationState.values()) {
            items.add(new SelectItem(s, s.getLabel()));
        }
        return items;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
