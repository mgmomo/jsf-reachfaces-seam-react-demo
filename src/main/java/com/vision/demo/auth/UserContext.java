package com.vision.demo.auth;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

@Name("userContext")
@Scope(ScopeType.SESSION)
public class UserContext implements Serializable {

    private static final long serialVersionUID = 1L;

    private String getSessionAttribute(String key) {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc != null) {
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
            if (session != null) {
                Object val = session.getAttribute(key);
                return val != null ? val.toString() : null;
            }
        }
        return null;
    }

    private UserRole getSessionRole() {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc != null) {
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
            if (session != null) {
                Object val = session.getAttribute(HeaderAuthFilter.SESSION_ROLE);
                if (val instanceof UserRole) {
                    return (UserRole) val;
                }
            }
        }
        return UserRole.GUEST;
    }

    public String getUsername() {
        String username = getSessionAttribute(HeaderAuthFilter.SESSION_USERNAME);
        return username != null ? username : "anonymous";
    }

    public UserRole getRole() {
        return getSessionRole();
    }

    public String getRoleLabel() {
        return getRole().name();
    }

    public boolean isAdmin() {
        return getRole().isAdmin();
    }

    public boolean isCanEdit() {
        return getRole().isAdmin();
    }

    public boolean isCanView() {
        return getRole().isAtLeast(UserRole.USER);
    }

    public boolean isGuest() {
        return getRole().isGuest();
    }

    public void requireRole(UserRole required) {
        if (!getRole().isAtLeast(required)) {
            throw new AuthorizationException(
                    "Access denied. Required role: " + required + ", current role: " + getRole());
        }
    }
}
