package com.finalProject.e_commerce.service;

import com.finalProject.e_commerce.domain.Cart;
import com.finalProject.e_commerce.domain.CartItem;
import com.finalProject.e_commerce.domain.Customer;
import com.finalProject.e_commerce.domain.Product;
import com.finalProject.e_commerce.dto.CartResponseDTO;
import com.finalProject.e_commerce.repository.CartRepo;
import com.finalProject.e_commerce.util.MapperUtil;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//@NoArgsConstructor
public class CartService {

    private final CartRepo cartRepo;
    private final ProductService productService;
    private final MapperUtil mapper;

    public CartResponseDTO getCart() {
        return mapper.mapEntityToResponseDTO(createNewCartOrGet());
    }

    public void addItemToCart(Long productId) {
        Cart cart = createNewCartOrGet();
        Product product = productService.getProductById(productId);
        if(product == null) {
            return;
        }

        if(cart.getCartItems().stream().anyMatch(item -> item.getProduct() == product)) {
            CartItem cartItemExists = cart.getCartItems()
                    .stream()
                    .filter(item -> item.getProduct() == product)
                    .toList().get(0);
            cartItemExists.setQuantity(cartItemExists.getQuantity() + 1);
            cartItemExists.setTotalPrice(cartItemExists.getQuantity() * cartItemExists.getProduct().getPrice());
            cart.calculateTotalPrice();
        }
        else {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setTotalPrice(cartItem.getProduct().getPrice());
            cart.addCartItem(cartItem);
        }
        cartRepo.save(cart);
    }

    public void removeItemFromCart(Long productId) {
        Cart cart = createNewCartOrGet();
        Product product = productService.getProductById(productId);
        if(product == null) {
            return;
        }
        CartItem cartItem = cart.getCartItems()
                    .stream()
                    .filter(item -> item.getProduct() == product)
                    .toList().get(0);

        cart.removeCartItem(cartItem);
        cartRepo.save(cart);
        }

        public void updateItemQuantity(Long productId, Integer quantity) {
            Cart cart = createNewCartOrGet();
            Product product = productService.getProductById(productId);
            if(product == null) {
                return;
            }
            CartItem cartItem = cart.getCartItems()
                    .stream()
                    .filter(item -> item.getProduct() == product)
                    .toList().get(0);
            if(quantity <= 0 || quantity.toString().isEmpty()) {
                cartItem.setQuantity(1);
            }
            else {
                cartItem.setQuantity(quantity);
            }
            cartItem.setTotalPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice());
            cart.calculateTotalPrice();
            cartRepo.save(cart);
        }

        public Cart createNewCartOrGet() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Customer customer = (Customer) authentication.getPrincipal();
            if(cartRepo.findByCustomerId(customer.getId()) == null) {
                Cart cart = new Cart();
                cart.setCustomer(customer);
                cartRepo.save(cart);
                return cart;
            }
            return cartRepo.findByCustomerId(customer.getId());
        }
}
