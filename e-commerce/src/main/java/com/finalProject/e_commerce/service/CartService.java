package com.finalProject.e_commerce.service;

import com.finalProject.e_commerce.domain.Cart;
import com.finalProject.e_commerce.domain.CartItem;
import com.finalProject.e_commerce.domain.Customer;
import com.finalProject.e_commerce.domain.Product;
import com.finalProject.e_commerce.dto.cartDTOs.CartItemResponseDTO;
import com.finalProject.e_commerce.dto.cartDTOs.CartResponseDTO;
import com.finalProject.e_commerce.repository.CartRepo;
import com.finalProject.e_commerce.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public ResponseEntity<?> addItemToCart(Long productId) {
        Cart cart = createNewCartOrGet();
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.status(401).body("Product not found");
        }
        CartItem cartItem;

        if (cart.getCartItems().stream().anyMatch(item -> item.getProduct() == product)) {
            cartItem = cart.getCartItems()
                    .stream()
                    .filter(item -> item.getProduct() == product)
                    .toList().get(0);
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItem.setTotalPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice());

            cart.calculateTotalPrice();
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setTotalPrice(cartItem.getProduct().getPrice());
            cart.addCartItem(cartItem);
        }
        if (cartItem.getQuantity() > product.getStock()) {
            return ResponseEntity.status(400).body("Stock not available");
        }
        cartRepo.save(cart);
        return ResponseEntity.status(200).body("Added item to cart");
    }

    public void removeItemFromCart(Long productId) {
        Cart cart = createNewCartOrGet();
        Product product = productService.getProductById(productId);
        if (product == null) {
            return;
        }
        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct() == product)
                .toList().get(0);

        cart.removeCartItem(cartItem);
        cartRepo.save(cart);
    }

        public int updateItemQuantity(Long productId, Integer quantity) {
            Cart cart = createNewCartOrGet();
            Product product = productService.getProductById(productId);
            if(product == null) {
                return -1;
            }
            CartItem cartItem = cart.getCartItems()
                    .stream()
                    .filter(item -> item.getProduct() == product)
                    .toList().get(0);
            if(quantity <= 0 || quantity.toString().isEmpty()) {
                cartItem.setQuantity(1);
            }
            else
            {
                cartItem.setQuantity(quantity);
            }
            if (quantity > product.getStock()){
                cartItem.setQuantity(product.getStock());
                cartItem.setTotalPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice());
                cart.calculateTotalPrice();
                cartRepo.save(cart);
                return product.getStock();
            }

            cartItem.setTotalPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice());
            cart.calculateTotalPrice();
            cartRepo.save(cart);
            return -2;
        }

    public Cart createNewCartOrGet() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = (Customer) authentication.getPrincipal();
        int flag = 0;
        if (cartRepo.findByCustomerId(customer.getId()) == null) {
            Cart cart = new Cart();
            cart.setCustomer(customer);
            cartRepo.save(cart);
            return cart;
        }
        Cart cart = cartRepo.findByCustomerId(customer.getId());
        for(CartItem cartItem : cart.getCartItems()) {
            if(cartItem.getProduct().getStock() <= 0) {
                cart.removeCartItem(cartItem);
                cartRepo.save(cart);
            }
            else if(cartItem.getProduct().getStock() < cartItem.getQuantity()){
                cartItem.setQuantity(cartItem.getProduct().getStock());
                cartRepo.save(cart);
            }
        }
        return cart;
    }

    public List<CartItemResponseDTO> getCartItems(long id) {
        Cart cart = cartRepo.findByCustomerId(id);
        List<CartItem> cartItems = cart.getCartItems();

        List<CartItemResponseDTO> cartItemDTOs = cartItems.stream()
                .map(mapper::mapEntityToDTO) // Example using a mapper or manual mapping
                .collect(Collectors.toList());

        return cartItemDTOs;
    }

    public void deleteAllByCustomerId(Long customerId) {
        cartRepo.deleteAllByCustomerId(customerId);
        Cart cart = cartRepo.findByCustomerId(customerId);
        cart.setTotalPrice(0);
        cartRepo.save(cart);
    }

}
