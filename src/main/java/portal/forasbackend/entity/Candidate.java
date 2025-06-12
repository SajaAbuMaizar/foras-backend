package portal.forasbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import portal.forasbackend.common.model.User;
import portal.forasbackend.enums.Gender;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "candidates")
public class Candidate implements User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phone;

    @ManyToOne(optional = false)
    @JoinColumn(name = "city_id")
    private City city;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "candidate_skills", joinColumns = @JoinColumn(name = "candidate_id"))
    @Column(name = "skill")
    private List<String> skills;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "candidate_languages", joinColumns = @JoinColumn(name = "candidate_id"))
    @Column(name = "language")
    private List<String> languages;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "candidate_licenses", joinColumns = @JoinColumn(name = "candidate_id"))
    @Column(name = "license")
    private List<String> driverLicenses;

    @Column(nullable = false)
    private boolean knowsHebrew;

    @Column(nullable = false)
    private boolean needsHelp;

    @Column
    private String avatarUrl;


    @Builder.Default
    private String role = "ROLE_CANDIDATE";

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
        return phone; // or phone if you use phone for login
    }

    @Override
    public String getPassword() {
        return password;
    }

}