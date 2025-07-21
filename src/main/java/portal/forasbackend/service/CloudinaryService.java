package portal.forasbackend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import portal.forasbackend.exception.FileUploadException;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;
    private final FileValidationService fileValidationService;

    public String uploadJobImage(MultipartFile imageFile) {
        try {
            fileValidationService.validateImageFile(imageFile, "job");

            String uniqueId = UUID.randomUUID().toString();
            Map uploadParams = ObjectUtils.asMap(
                    "folder", "jobPortal/jobs",
                    "public_id", "job_image_" + uniqueId,
                    "transformation", new Transformation()
                            .width(330)
                            .height(180)
                            .crop("fit")
                            .quality("auto:best"),
                    "resource_type", "image",
                    "allowed_formats", new String[]{"jpg", "png", "gif", "webp"}
            );

            Map<?, ?> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), uploadParams);
            String url = uploadResult.get("secure_url").toString();
            log.info("Successfully uploaded job image: {}", url);
            return url;
        } catch (FileUploadException e) {
            throw e;
        } catch (IOException e) {
            log.error("IO error uploading job image to Cloudinary", e);
            throw new FileUploadException("فشل في رفع الصورة. يرجى المحاولة مرة أخرى", "UPLOAD_FAILED", e);
        } catch (Exception e) {
            log.error("Unexpected error uploading job image", e);
            throw new FileUploadException("حدث خطأ غير متوقع أثناء رفع الصورة", "UNEXPECTED_ERROR", e);
        }
    }

    public String uploadEmployerLogo(MultipartFile file) {
        try {
            fileValidationService.validateImageFile(file, "logo");

            String uniqueId = UUID.randomUUID().toString();
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "folder", "jobPortal/employers/logos",
                    "public_id", "employer_logo_" + uniqueId,
                    "transformation", new Transformation()
                            .width(200)
                            .height(200)
                            .crop("fit")
                            .quality("auto:good"),
                    "resource_type", "image",
                    "allowed_formats", new String[]{"jpg", "png", "gif", "webp"}
            );

            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            String url = uploadResult.get("secure_url").toString();
            log.info("Successfully uploaded employer logo: {}", url);
            return url;
        } catch (FileUploadException e) {
            throw e;
        } catch (IOException e) {
            log.error("IO error uploading employer logo to Cloudinary", e);
            throw new FileUploadException("فشل في رفع الشعار. يرجى المحاولة مرة أخرى", "UPLOAD_FAILED", e);
        } catch (Exception e) {
            log.error("Unexpected error uploading employer logo", e);
            throw new FileUploadException("حدث خطأ غير متوقع أثناء رفع الشعار", "UNEXPECTED_ERROR", e);
        }
    }

    public String uploadCandidateAvatar(MultipartFile file) {
        try {
            fileValidationService.validateImageFile(file, "avatar");

            String uniqueId = UUID.randomUUID().toString();
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "folder", "jobPortal/candidates/avatars",
                    "public_id", "candidate_avatar_" + uniqueId,
                    "transformation", new Transformation()
                            .width(400)
                            .height(400)
                            .crop("fill")
                            .gravity("face")
                            .quality("auto:best"),
                    "resource_type", "image",
                    "allowed_formats", new String[]{"jpg", "png", "gif", "webp"}
            );

            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            String url = uploadResult.get("secure_url").toString();
            log.info("Successfully uploaded candidate avatar: {}", url);
            return url;
        } catch (FileUploadException e) {
            throw e;
        } catch (IOException e) {
            log.error("IO error uploading candidate avatar to Cloudinary", e);
            throw new FileUploadException("فشل في رفع الصورة الشخصية. يرجى المحاولة مرة أخرى", "UPLOAD_FAILED", e);
        } catch (Exception e) {
            log.error("Unexpected error uploading candidate avatar", e);
            throw new FileUploadException("حدث خطأ غير متوقع أثناء رفع الصورة الشخصية", "UNEXPECTED_ERROR", e);
        }
    }
}