package com.example.demo.service;

import com.example.demo.exception.DuplicateWishlistItemException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.WishlistItem;
import com.example.demo.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepository repository;

    public WishlistService(WishlistRepository repository) {
        this.repository = repository;
    }

    public WishlistItem addItem(Long userId, Long productId) {
        repository.findByUserIdAndProductId(userId, productId)
                .ifPresent(item -> {
                    throw new DuplicateWishlistItemException("Item already in wishlist");
                });

        WishlistItem item = new WishlistItem(null, userId, productId);
        return repository.save(item);
    }

    public List<WishlistItem> getUserWishlist(Long userId) {
        List<WishlistItem> wishlist = repository.findByUserId(userId);
        if (wishlist.isEmpty()) {
            throw new UserNotFoundException("User not found or no wishlist items available for user " + userId);
        }
        return wishlist;
    }

    public void removeItem(Long userId, Long productId) {
        repository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("Item not found in wishlist"));

        repository.deleteByUserIdAndProductId(userId, productId);
    }

    public boolean isInWishlist(Long userId, Long productId) {
        return repository.findByUserIdAndProductId(userId, productId).isPresent();
    }
}
