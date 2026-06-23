package com.ivs.usermanager.common.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.Normalizer;
import java.util.List;
import java.util.Map;

/**
 * Service responsible for uploading images to Cloudinary.
 */
@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.default-folder:uploads}")
    private String defaultFolder;

    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;

    private static final List<String> SUPPORTED_IMAGE_MIME = List.of(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/webp"
    );

    /**
     * Uploads an avatar image to Cloudinary.
     *
     * <p>
     * Validation rules:
     *
     * @param file avatar image file
     * @return secure URL of the uploaded image
     * @throws RuntimeException if validation fails or upload fails
     */
    public String uploadAvatar(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Image file is required");
        }

        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new RuntimeException("Image size must be less than 5MB");
        }

        String contentType = file.getContentType();

        if (contentType == null || !SUPPORTED_IMAGE_MIME.contains(contentType.toLowerCase())) {
            throw new RuntimeException("Only JPG, JPEG, PNG, WEBP images are allowed");
        }

        try {
            String publicId = generatePublicId(file.getOriginalFilename());

            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", defaultFolder,
                            "resource_type", "image",
                            "public_id", publicId,
                            "use_filename", false,
                            "unique_filename", true,
                            "overwrite", false
                    )
            );

            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Upload image failed: " + e.getMessage());
        }
    }

    /**
     * Generates a sanitized public ID from the original file name.
     *
     * <p>
     * The generated ID:
     * <ul>
     *     <li>Removes file extension.</li>
     *     <li>Removes accents and special characters.</li>
     *     <li>Converts spaces and underscores to hyphens.</li>
     *     <li>Limits length to 120 characters.</li>
     * </ul>
     *
     * @param originalName original file name
     * @return sanitized public ID
     */
    private String generatePublicId(String originalName) {

        if (originalName == null || originalName.isBlank()) {
            return "avatar";
        }

        String name = originalName;

        int dotIndex = name.lastIndexOf(".");
        if (dotIndex > 0) {
            // Remove file extension before generating the public ID
            name = name.substring(0, dotIndex);
        }

        // Convert accented characters (e.g. tiếng Việt) to ASCII characters
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        // Normalize the file name to make it URL-safe and Cloudinary-friendly
        String publicId = normalized
                .trim()
                .toLowerCase()
                .replaceAll("[\\s_]+", "-")
                .replaceAll("[^a-z0-9-]+", "")
                .replaceAll("-+", "-");

        // Cloudinary public IDs should not be excessively long
        if (publicId.length() > 120) {
            publicId = publicId.substring(0, 120);
        }

        return publicId.isBlank() ? "avatar" : publicId;
    }
}