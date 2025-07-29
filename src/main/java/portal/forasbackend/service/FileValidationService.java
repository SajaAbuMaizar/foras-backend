package portal.forasbackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.core.exceptions.FileUploadException;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class FileValidationService {

    private static final long MAX_FILE_SIZE = 1 * 1024 * 1024; // 1MB
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/gif",
            "image/webp"
    );
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            "jpg", "jpeg", "png", "gif", "webp"
    );

    public void validateImageFile(MultipartFile file, String fileType) {
        if (file == null || file.isEmpty()) {
            throw new FileUploadException("الملف فارغ أو غير موجود", "EMPTY_FILE");
        }

        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            String sizeInMB = String.format("%.2f", file.getSize() / (1024.0 * 1024.0));
            throw new FileUploadException(
                    String.format("حجم الملف (%s MB) يتجاوز الحد المسموح به (1MB)", sizeInMB),
                    "FILE_TOO_LARGE"
            );
        }

        // Check content type
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new FileUploadException(
                    "نوع الملف غير مدعوم. الأنواع المدعومة: JPG, PNG, GIF, WEBP",
                    "INVALID_FILE_TYPE"
            );
        }

        // Check file extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            String extension = getFileExtension(originalFilename).toLowerCase();
            if (!ALLOWED_EXTENSIONS.contains(extension)) {
                throw new FileUploadException(
                        "امتداد الملف غير مدعوم. الامتدادات المدعومة: " + String.join(", ", ALLOWED_EXTENSIONS),
                        "INVALID_FILE_EXTENSION"
                );
            }
        }

        log.info("File validation successful for {} upload: size={} bytes, type={}",
                fileType, file.getSize(), contentType);
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(lastDotIndex + 1) : "";
    }
}