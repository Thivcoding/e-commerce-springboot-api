package org.hokvanthiv.ecommerce_springboot_api.service.Impl;

import lombok.RequiredArgsConstructor;
import org.hokvanthiv.ecommerce_springboot_api.dto.request.CartItemRequestDTO;
import org.hokvanthiv.ecommerce_springboot_api.dto.response.CartResponseDTO;
import org.hokvanthiv.ecommerce_springboot_api.entity.Cart;
import org.hokvanthiv.ecommerce_springboot_api.entity.CartItem;
import org.hokvanthiv.ecommerce_springboot_api.entity.Product;
import org.hokvanthiv.ecommerce_springboot_api.entity.User;
import org.hokvanthiv.ecommerce_springboot_api.exception.ResourceNotFoundException;
import org.hokvanthiv.ecommerce_springboot_api.mapper.CartMapper;
import org.hokvanthiv.ecommerce_springboot_api.repository.CartItemRepository;
import org.hokvanthiv.ecommerce_springboot_api.repository.CartRepository;
import org.hokvanthiv.ecommerce_springboot_api.repository.ProductRepository;
import org.hokvanthiv.ecommerce_springboot_api.repository.UserRepository;
import org.hokvanthiv.ecommerce_springboot_api.service.CartItemService;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartItemServiceImpl(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // =========================
    // ADD ITEM TO CART
    // =========================
    @Override
    public CartResponseDTO addItemToCart(
            String email,
            CartItemRequestDTO request
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        Product product = productRepository
                .findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found"
                        ));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {

                    Cart newCart = new Cart();

                    newCart.setUser(user);

                    return cartRepository.save(newCart);
                });

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(
                        cart.getId(),
                        product.getId()
                )
                .orElse(null);

        if (item != null) {

            item.setQuantity(
                    item.getQuantity() + request.getQuantity()
            );

        } else {

            item = new CartItem();

            item.setCart(cart);
            item.setProduct(product);

            item.setQuantity(request.getQuantity());

            item.setPrice(product.getPrice());
        }

        cartItemRepository.save(item);

        return CartMapper.toDTO(cart);
    }

    // =========================
    // UPDATE ITEM QUANTITY
    // =========================
    @Override
    public CartResponseDTO updateCartItem(
            String email,
            Long itemId,
            Integer quantity
    ) {

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart item not found"
                        ));

        // ownership check
        if (!item.getCart()
                .getUser()
                .getEmail()
                .equals(email)) {

            throw new RuntimeException(
                    "Access denied"
            );
        }

        item.setQuantity(quantity);

        cartItemRepository.save(item);

        return CartMapper.toDTO(item.getCart());
    }

    // =========================
    // REMOVE ITEM
    // =========================
    @Override
    public CartResponseDTO removeItemFromCart(
            String email,
            Long itemId
    ) {

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart item not found"
                        ));

        // ownership check
        if (!item.getCart()
                .getUser()
                .getEmail()
                .equals(email)) {

            throw new RuntimeException(
                    "Access denied"
            );
        }

        Cart cart = item.getCart();

        cartItemRepository.delete(item);

        return CartMapper.toDTO(cart);
    }
}