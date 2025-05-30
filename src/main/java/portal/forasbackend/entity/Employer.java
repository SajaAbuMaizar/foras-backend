package portal.forasbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import portal.forasbackend.common.model.JwtUserDetails;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employers")
public class Employer implements JwtUserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String companyName;
    private String email;
    private String phone;
    private String password;

    // Add a property to store the Cloudinary image URL
    private String companyLogoUrl;

    private String role = "ROLE_EMPLOYER";


}
