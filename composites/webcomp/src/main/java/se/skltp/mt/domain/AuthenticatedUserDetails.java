package se.skltp.messagebox.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticatedUserDetails {

    private static final Logger log = LoggerFactory.getLogger(AuthenticatedUserDetails.class);

    /**
     * Returns the username of an authenticated user.
     * 
     * @return the username if the user is logged in otherwise null
     */
    public static String getPrincipal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("Getting authorized user: {}.", new Object[] { principal });
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return null;

        }
    }
    
    public static boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }
}
