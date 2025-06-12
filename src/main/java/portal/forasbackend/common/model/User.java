package portal.forasbackend.common.model;

import org.springframework.security.core.userdetails.UserDetails;

public interface User extends UserDetails {
    Long getId();
    String getName();
    String getRole();

    @Override
    default boolean isAccountNonExpired() {
        return true;
    }

    @Override
    default boolean isAccountNonLocked() {
        return true;
    }

    @Override
    default boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    default boolean isEnabled() {
        return true;
    }
}
