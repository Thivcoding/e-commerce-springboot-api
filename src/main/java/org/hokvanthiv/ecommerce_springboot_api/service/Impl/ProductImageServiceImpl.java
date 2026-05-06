package org.hokvanthiv.ecommerce_springboot_api.service.Impl;

import org.hokvanthiv.ecommerce_springboot_api.dto.response.CloudinaryResponse;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.ProductImageResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.entity.Product;
import org.hokvanthiv.ecommerce_springboot_api.entity.ProductImage;
import org.hokvanthiv.ecommerce_springboot_api.exception.ResourceNotFoundException;
import org.hokvanthiv.ecommerce_springboot_api.mapper.ProductMapper;
import org.hokvanthiv.ecommerce_springboot_api.repository.ProductImageRepository;
import org.hokvanthiv.ecommerce_springboot_api.repository.ProductRepository;
import org.hokvanthiv.ecommerce_springboot_api.service.CloudinaryService;
import org.hokvanthiv.ecommerce_springboot_api.service.ProductImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CloudinaryService cloudinaryService;

    public ProductImageServiceImpl(
            ProductRepository productRepository,
            ProductImageRepository productImageRepository,
            CloudinaryService cloudinaryService
    ) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.cloudinaryService = cloudinaryService;
    }

    // ADD IMAGE
    @Override
    public ProductImageResponseDTO addImage(Long productId, MultipartFile file) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if(file == null || file.isEmpty()){
            throw new IllegalArgumentException("File is empty");
        }

        CloudinaryResponse upload = cloudinaryService.uploadFile(file);

        ProductImage image = new ProductImage();
        image.setImageUrl(upload.getUrl());
        image.setPublicId(upload.getPublicId());
        image.setProduct(product);

        ProductImage saved = productImageRepository.save(image);

        return ProductMapper.mapImageToDTO(saved);
    }

    // GET IMAGES BY PRODUCT
    @Override
    public List<ProductImageResponseDTO> getImagesByProduct(Long productId) {

        List<ProductImage> images = productImageRepository.findByProductId(productId);

        return ProductMapper.imageToDTOList(images);
    }

    // UPDATE IMAGE BY ID
    @Override
    public ProductImageResponseDTO replaceImage(Long imageId, MultipartFile file) {

        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));

        if(file != null && !file.isEmpty()){
            // delete old file
            if(image.getPublicId() != null && !image.getPublicId().isEmpty()){
                cloudinaryService.deleteFile(image.getPublicId());
            }

            // upload new file
            CloudinaryResponse upload = cloudinaryService.uploadFile(file);

            image.setImageUrl(upload.getUrl());
            image.setPublicId(upload.getPublicId());
        }

        ProductImage saved = productImageRepository.save(image);

        return ProductMapper.mapImageToDTO(saved);
    }

    // DELETE SINGLE IMAGE
    @Override
    public void deleteImage(Long imageId) {

        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));

        // delete from cloudinary
        cloudinaryService.deleteFile(image.getPublicId());

        // delete from DB
        productImageRepository.delete(image);
    }
}
