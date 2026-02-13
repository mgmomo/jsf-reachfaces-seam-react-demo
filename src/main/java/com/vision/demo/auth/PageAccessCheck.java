package com.vision.demo.auth;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import java.io.Serializable;

@Name("pageAccessCheck")
@Scope(ScopeType.EVENT)
public class PageAccessCheck implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserContext getUserContext() {
        return (UserContext) Component.getInstance("userContext");
    }

    public String requireView() {
        UserContext uc = getUserContext();
        if (uc == null || uc.isGuest()) {
            return "/accessDenied.xhtml";
        }
        return null;
    }
}
