package org.hokvanthiv.ecommerce_springboot_api.service.Impl;

import org.hokvanthiv.ecommerce_springboot_api.dto.request.ProductRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.CloudinaryResponse;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.ProductResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.entity.Category;
import org.hokvanthiv.ecommerce_springboot_api.entity.Product;
import org.hokvanthiv.ecommerce_springboot_api.entity.ProductImage;
import org.hokvanthiv.ecommerce_springboot_api.exception.ResourceNotFoundException;
import org.hokvanthiv.ecommerce_springboot_api.mapper.ProductMapper;
import org.hokvanthiv.ecommerce_springboot_api.repository.CategoryRepository;
import org.hokvanthiv.ecommerce_springboot_api.repository.ProductImageRepository;
import org.hokvanthiv.ecommerce_springboot_api.repository.ProductRepository;
import org.hokvanthiv.ecommerce_springboot_api.service.CloudinaryService;
import org.hokvanthiv.ecommerce_springboot_api.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private CloudinaryService cloudinaryService;
    private ProductImageRepository productImageRepository;

    public ProductServiceImpl(
            ProductRepository productRepository
            ,CategoryRepository categoryRepository
            ,CloudinaryService cloudinaryService
            ,ProductImageRepository productImageRepository
    ){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.cloudinaryService = cloudinaryService;
        this.productImageRepository = productImageRepository;
    }

    // CREATE PRODUCT + UPLOAD IMAGE ON CLOUDINARY
    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO request) {

        // Find Category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + request.getCategoryId()
                ));

        // Convert DTO → Entity
        Product product = ProductMapper.toEntity(request, category);

        // Save Product first
        product = productRepository.save(product);

        // Upload Images + Save DB
        if (request.getImages() != null && !request.getImages().isEmpty()) {

                List<ProductImage> images = new ArrayList<>();

                for (MultipartFile file : request.getImages()) {

                    CloudinaryResponse upload = cloudinaryService.uploadFile(file);

                    ProductImage image = new ProductImage();
                    image.setImageUrl(upload.getUrl());
                    image.setPublicId(upload.getPublicId());
                    image.setProduct(product);

                    images.add(image);
                }

                product.setImages(images);

                productImageRepository.saveAll(images);
        }

        // IMPORTANT: reload product with images
        Product savedProduct = productRepository.findByIdWithImages(product.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Return full response
        return ProductMapper.toDTO(savedProduct);
    }

    // GET ALL PRODUCT
    @Override
    public List<ProductResponseDTO> getAllProduct() {

        List<Product> products = productRepository.findAllWithImages();

        return products.stream()
                .map(ProductMapper::toDTO)
                .toList();
    }

    // GET PRODUCT BY ID
    @Override
    public ProductResponseDTO getProductById(Long id){

        Product product = productRepository.findByIdWithImages(id).
                orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return ProductMapper.toDTO(product);
    };

    // UPDATE PRODUCT
    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO request){

        // Find Category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        // Find Product
        Product product = productRepository.findByIdWithImages(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Update basic info
        ProductMapper.updateEntity(product, request, category);

        // Handle Images (DELETE OLD + ADD NEW)
        if (request.getImages() != null && !request.getImages().isEmpty()) {

            // DELETE OLD IMAGES FROM CLOUDINARY + DB
            if (product.getImages() != null && !product.getImages().isEmpty()) {

                for (ProductImage img : product.getImages()) {
                    cloudinaryService.deleteFile(img.getPublicId());
                }

                productImageRepository.deleteAll(product.getImages());
                product.getImages().clear();
            }

            // UPLOAD NEW IMAGES
            List<ProductImage> newImages = new ArrayList<>();

            for (MultipartFile file : request.getImages()) {

                CloudinaryResponse upload = cloudinaryService.uploadFile(file);

                ProductImage image = new ProductImage();
                image.setImageUrl(upload.getUrl());
                image.setPublicId(upload.getPublicId());
                image.setProduct(product);

                newImages.add(image);
            }

            product.setImages(newImages);
            productImageRepository.saveAll(newImages);
        }

        // Save product
        productRepository.save(product);

        // Reload with images
        Product updatedProduct = productRepository.findByIdWithImages(product.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Return DTO
        return ProductMapper.toDTO(updatedProduct);
    }

    // DELETE PRODUCT
    @Override
    public void deleteProduct(Long id){

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // DELETE IMAGES FROM CLOUDINARY + DB
        if (product.getImages() != null && !product.getImages().isEmpty()) {

            for (ProductImage img : product.getImages()) {
                cloudinaryService.deleteFile(img.getPublicId());
            }

            productImageRepository.deleteAll(product.getImages());
            product.getImages().clear();
        }

        // DELETE PRODUCT
        productRepository.delete(product);
    }
}
