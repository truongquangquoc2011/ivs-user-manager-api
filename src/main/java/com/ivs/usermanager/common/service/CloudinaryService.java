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

    private String generatePublicId(String originalName) {
        if (originalName == null || originalName.isBlank()) {
            return "avatar";
        }

        String name = originalName;

        int dotIndex = name.lastIndexOf(".");
        if (dotIndex > 0) {
            name = name.substring(0, dotIndex);
        }

        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        String publicId = normalized
                .trim()
                .toLowerCase()
                .replaceAll("[\\s_]+", "-")
                .replaceAll("[^a-z0-9-]+", "")
                .replaceAll("-+", "-");

        if (publicId.length() > 120) {
            publicId = publicId.substring(0, 120);
        }

        return publicId.isBlank() ? "avatar" : publicId;
    }
}