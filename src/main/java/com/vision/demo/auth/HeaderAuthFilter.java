package com.vision.demo.auth;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class HeaderAuthFilter implements Filter {

    public static final String SESSION_USERNAME = "auth.username";
    public static final String SESSION_ROLE = "auth.role";

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(true);

        String userHeader = httpRequest.getHeader("X-Remote-User");
        String roleHeader = httpRequest.getHeader("X-Remote-Roles");

        String username;
        UserRole role;

        if (userHeader == null || userHeader.trim().isEmpty()) {
            username = "anonymous";
            role = UserRole.GUEST;
        } else {
            username = userHeader.trim();
            role = UserRole.fromHeader(roleHeader);
        }

        session.setAttribute(SESSION_USERNAME, username);
        session.setAttribute(SESSION_ROLE, role);

        chain.doFilter(request, response);
    }

    public void destroy() {
    }
}
