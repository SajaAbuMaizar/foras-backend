package portal.forasbackend.config.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dr1xulz8a",
                "api_key", "681843879894419",
                "api_secret", "81W87OqCu4oO3RXYbNURNCQe9g8",
                "secure", true
        ));
    }
}
