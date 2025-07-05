package portal.forasbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import portal.forasbackend.common.model.User;

import java.util.Collection;
import java.util.List;
import java.time.LocalDateTime;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employers")
public class Employer implements User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String companyName;
    private String email;
    private String phone;
    private String password;
    private String companyLogoUrl;
    private String preferredLanguage; // "ar" or "he"

    private LocalDateTime lastLogin;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    private String role = "ROLE_EMPLOYER";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
}