package com.samsungsds.eshop.cart;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api; //👈👈  add line
import io.swagger.annotations.ApiOperation; //👈👈  add line

 

@RestController
@RequestMapping(value = "/api/carts")
@Api(tags = "shopping basket", description = "Shopping Cart API") //👈👈  add line
public class CartController {
    private final CartService cartService;

    public CartController(final CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping(value = "/empty")
    @ApiOperation(value = "Reset shopping cart") //👈👈  add line
    public ResponseEntity<Void> emptyCart() {
        cartService.emptyCart();
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @ApiOperation(value = "Add to Cart") //👈👈  add line
    public ResponseEntity<Void> addToCart(@RequestBody CartItem cartItem) {
        cartService.addToCart(cartItem);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @ApiOperation(value = "Shopping Cart View") //👈👈  add line
    public ResponseEntity<List<CartItem>> getCartItems() {
        List<CartItem> cartItems = cartService.getCartItems();
        return ResponseEntity.ok(cartItems);
    }

    @GetMapping(value = "/count")
    @ApiOperation(value = "Check the number of shopping carts") //👈👈  add line
    public ResponseEntity<Long> getCartItemsCount() {
        Long cartItemCount = cartService.getCartItemsCount();
        return ResponseEntity.ok(cartItemCount);
    }
}