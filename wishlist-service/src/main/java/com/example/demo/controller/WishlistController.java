package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.demo.model.WishlistItem;
import com.example.demo.service.WishlistService;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService service;

    public WishlistController(WishlistService service) {
        this.service = service;
    }

    @PostMapping
    public WishlistItem add(@RequestParam Long userId, @RequestParam Long productId) {
        return service.addItem(userId, productId);
    }

    @GetMapping("/{userId}")
    public List<WishlistItem> get(@PathVariable Long userId) {
        return service.getUserWishlist(userId);
    }

    @DeleteMapping
    public void delete(@RequestParam Long userId, @RequestParam Long productId) {
        service.removeItem(userId, productId);
    }

    @GetMapping("/check")
    public boolean check(@RequestParam Long userId, @RequestParam Long productId) {
        return service.isInWishlist(userId, productId);
    }
}
