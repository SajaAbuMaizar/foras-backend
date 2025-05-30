package portal.forasbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import portal.forasbackend.common.model.JwtUserDetails;
import portal.forasbackend.enums.Gender;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "candidates")
public class Candidate implements JwtUserDetails {

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

    @Builder.Default
    private String role = "ROLE_CANDIDATE";

}