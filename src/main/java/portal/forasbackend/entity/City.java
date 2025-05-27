package portal.forasbackend.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // internal name, e.g. JERUSALEM

    @Column(nullable = false)
    private String nameAr; // Arabic name

    @Column(nullable = false)
    private String nameHe; // Hebrew name
}