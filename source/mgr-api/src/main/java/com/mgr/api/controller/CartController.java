package com.mgr.api.controller;

import com.mgr.api.dto.ApiMessageDto;
import com.mgr.api.dto.ErrorCode;
import com.mgr.api.dto.cart.CartDto;
import com.mgr.api.exception.BadRequestException;
import com.mgr.api.exception.NotFoundException;
import com.mgr.api.form.cart.AddCartItemForm;
import com.mgr.api.form.cart.RemoveCartItemForm;
import com.mgr.api.form.cart.UpdateCartItemForm;
import com.mgr.api.mapper.CartMapper;
import com.mgr.api.model.*;
import com.mgr.api.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/cart")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CartController extends ABasicController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CartMapper cartMapper;

    private Cart getOrCreateCart() {
        Long accountId = getCurrentUser();
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found", ErrorCode.ACCOUNT_ERROR_NOT_FOUND));

        return cartRepository.findByAccount(account).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setAccount(account);
            return cartRepository.save(newCart);
        });
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CRT_C')")
    @Transactional
    public ApiMessageDto<Void> addToCart(@Valid @RequestBody AddCartItemForm form, BindingResult bindingResult) {
        Cart cart = getOrCreateCart();
        Product product = productRepository.findById(form.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found", ErrorCode.PRODUCT_ERROR_NOT_FOUND));

        CartItem item = cartItemRepository.findByCartAndProduct(cart, product).orElse(null);
        int currentQuantityInCart = (item != null) ? item.getQuantity() : 0;

        if (product.getStock() < currentQuantityInCart + form.getQuantity()) {
            throw new BadRequestException("Not enough stock", ErrorCode.PRODUCT_ERROR_OUT_OF_STOCK);
        }

        if (item == null) {
            item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(form.getQuantity());
        } else {
            item.setQuantity(item.getQuantity() + form.getQuantity());
        }

        cartItemRepository.save(item);
        return makeSuccessResponse("Added to cart");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CRT_U')")
    @Transactional
    public ApiMessageDto<Void> updateCartItem(@Valid @RequestBody UpdateCartItemForm form, BindingResult bindingResult) {
        CartItem item = cartItemRepository.findById(form.getCartItemId())
                .orElseThrow(() -> new NotFoundException("Item not found", ErrorCode.CART_ERROR_ITEM_NOT_FOUND));

        if (!item.getCart().getAccount().getId().equals(getCurrentUser())) {
            throw new NotFoundException("Item not found", ErrorCode.CART_ERROR_ITEM_NOT_FOUND);
        }

        if (item.getProduct().getStock() < form.getQuantity()) {
            throw new BadRequestException("Not enough stock", ErrorCode.PRODUCT_ERROR_OUT_OF_STOCK);
        }

        item.setQuantity(form.getQuantity());
        cartItemRepository.save(item);
        return makeSuccessResponse("Cart updated");
    }

    @GetMapping(value = "/my-cart", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CRT_V')")
    public ApiMessageDto<CartDto> getMyCart() {
        Cart cart = getOrCreateCart();
        return makeSuccessResponse(cartMapper.fromEntityToCartDto(cart), "Get cart success");
    }

    @DeleteMapping(value = "/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CRT_D')")
    @Transactional
    public ApiMessageDto<Void> removeFromCart(@Valid @RequestBody RemoveCartItemForm form, BindingResult bindingResult) {
        CartItem item = cartItemRepository.findById(form.getCartItemId())
                .orElseThrow(() -> new NotFoundException("Item not found", ErrorCode.CART_ERROR_ITEM_NOT_FOUND));

        if (!item.getCart().getAccount().getId().equals(getCurrentUser())) {
            throw new NotFoundException("Item not found", ErrorCode.CART_ERROR_ITEM_NOT_FOUND);
        }

        cartItemRepository.delete(item);
        return makeSuccessResponse("Item removed");
    }
}