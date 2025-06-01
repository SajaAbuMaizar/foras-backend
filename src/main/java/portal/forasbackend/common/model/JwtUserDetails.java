package portal.forasbackend.common.model;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtUserDetails extends UserDetails {
    Long getId();
    String getName();
    String getRole();
}
