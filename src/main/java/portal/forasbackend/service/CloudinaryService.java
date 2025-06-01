package portal.forasbackend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import com.cloudinary.utils.ObjectUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadJobImage(MultipartFile imageFile) {
        if (imageFile.isEmpty()) {
            return null;
        }

        String uniqueId = UUID.randomUUID().toString();
        Map<String, Object> uploadParams = ObjectUtils.asMap(
                "folder", "jobPortal",
                "public_id", "job_image_" + uniqueId,
                "transformation", new Transformation()
                        .width(330)
                        .height(180)
                        .crop("fit")
                        .quality("auto:best")
        );

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), uploadParams);
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            log.error("Error uploading image to Cloudinary", e);
            throw new RuntimeException("Failed to upload image. Please try again.");
        }
    }
}
