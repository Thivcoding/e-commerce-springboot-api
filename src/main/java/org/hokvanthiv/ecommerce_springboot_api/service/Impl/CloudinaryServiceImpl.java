package org.hokvanthiv.ecommerce_springboot_api.service.Impl;

import org.hokvanthiv.ecommerce_springboot_api.dto.response.CloudinaryResponse;
import org.hokvanthiv.ecommerce_springboot_api.service.CloudinaryService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary){
        this.cloudinary = cloudinary;
    }

    @Override
    public CloudinaryResponse uploadFile(MultipartFile file) {
        try {

            //  Validate file
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("File is empty");
            }

            if (file.getContentType() == null ||
                    !file.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("Only image files allowed");
            }

            //  Upload
            Map<String, Object> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "ecommerce-springboot/products/image",
                            "public_id", "product_" + System.currentTimeMillis(),
                            "quality", "auto",
                            "fetch_format", "auto"
                    )
            );

            String url = result.get("secure_url").toString();
            String publicId = result.get("public_id").toString();

            return new CloudinaryResponse(url, publicId);

        } catch (Exception e) {
            throw new RuntimeException("Cloudinary upload failed", e);
        }
    }

    @Override
    public void deleteFile(String publicId) {
        try {

            if (publicId == null || publicId.isBlank()) {
                throw new IllegalArgumentException("PublicId is required");
            }

            Map<String, Object> result = cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.emptyMap()
            );

            String status = result.get("result").toString();

            if (!status.equals("ok") && !status.equals("not found")) {
                throw new RuntimeException("Cloudinary delete failed");
            }

        } catch (Exception e) {
            throw new RuntimeException("Cloudinary delete failed", e);
        }
    }
}