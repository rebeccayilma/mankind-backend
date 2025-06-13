package com.mankind.matrix_wishlistservice.service;

import com.mankind.matrix_wishlistservice.exception.DuplicateWishlistItemException;
import com.mankind.matrix_wishlistservice.exception.ItemNotInWishlistException;
import com.mankind.matrix_wishlistservice.exception.UserNotFoundException;
import com.mankind.matrix_wishlistservice.model.WishlistItem;
import com.mankind.matrix_wishlistservice.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class WishlistService {

    private final WishlistRepository repository;
    private final RestTemplate restTemplate;

    @Value("${product.service.url:http://localhost:8080}")
    private String productServiceUrl;

    public WishlistService(WishlistRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    /**
     * Fetches product details and price from the product service
     * @param productId The ID of the product to fetch
     * @return A WishlistItem with product details and price
     */
    private WishlistItem fetchProductDetails(Long productId) {
        try {
            // Fetch product details
            ResponseEntity<Map> productResponse = restTemplate.getForEntity(
                productServiceUrl + "/api/v1/products/" + productId,
                Map.class
            );

            Map<String, Object> productData = productResponse.getBody();

            // Fetch inventory details (for price)
            ResponseEntity<Map> inventoryResponse = restTemplate.getForEntity(
                productServiceUrl + "/api/v1/inventory/" + productId,
                Map.class
            );

            Map<String, Object> inventoryData = inventoryResponse.getBody();

            // Extract product details
            String name = (String) productData.get("name");
            String brand = (String) productData.get("brand");
            BigDecimal price = new BigDecimal(inventoryData.get("price").toString());

            // Get first image URL if available
            String imageUrl = null;
            if (productData.containsKey("images") && ((List)productData.get("images")).size() > 0) {
                imageUrl = (String) ((List)productData.get("images")).get(0);
            }

            // Create a placeholder for discounted price (same as regular price for now)
            BigDecimal discountedPrice = price;

            // Create placeholders for rating and review count
            Float rating = 0.0f;
            Integer reviewCount = 0;

            // Create and return the WishlistItem
            return new WishlistItem(null, null, productId, name, brand, price, discountedPrice, imageUrl, rating, reviewCount);
        } catch (Exception e) {
            // If there's an error, return a WishlistItem with null values
            return new WishlistItem(null, null, productId, null, null, null, null, null, 0.0f, 0);
        }
    }

    @Transactional
    public WishlistItem addItem(Long userId, Long productId, String name, String brand, java.math.BigDecimal price, 
                              java.math.BigDecimal discountedPrice, String imageUrl, Float rating, Integer reviewCount) {
        repository.findByUserIdAndProductId(userId, productId)
                .ifPresent(item -> {
                    throw new DuplicateWishlistItemException("Item already in wishlist");
                });

        // If product details are not provided, fetch them from the product service
        if (name == null || brand == null || price == null) {
            WishlistItem productDetails = fetchProductDetails(productId);
            name = productDetails.getName();
            brand = productDetails.getBrand();
            price = productDetails.getPrice();
            imageUrl = productDetails.getImageUrl();

            // Use provided values or defaults from product service
            discountedPrice = discountedPrice != null ? discountedPrice : productDetails.getDiscountedPrice();
            rating = rating != null ? rating : productDetails.getRating();
            reviewCount = reviewCount != null ? reviewCount : productDetails.getReviewCount();
        } else {
            // Use provided values or defaults
            discountedPrice = discountedPrice != null ? discountedPrice : price;
            rating = rating != null ? rating : 0.0f;
            reviewCount = reviewCount != null ? reviewCount : 0;
        }

        // Create and save the item with all fields
        WishlistItem item = new WishlistItem(null, userId, productId, name, brand, price, 
                                           discountedPrice, imageUrl, rating, reviewCount);
        return repository.save(item);
    }

    @Transactional
    public WishlistItem addItem(Long userId, Long productId, String name, String brand, java.math.BigDecimal price, String imageUrl) {
        // For backward compatibility
        return addItem(userId, productId, name, brand, price, null, imageUrl, null, null);
    }

    @Transactional
    public WishlistItem addItem(Long userId, Long productId) {
        // For backward compatibility, fetch product details from the product service
        WishlistItem productDetails = fetchProductDetails(productId);
        return addItem(userId, productId, productDetails.getName(), productDetails.getBrand(), 
                      productDetails.getPrice(), productDetails.getImageUrl());
    }

    public List<WishlistItem> getUserWishlist(Long userId) {
        List<WishlistItem> wishlist = repository.findByUserId(userId);
        if (wishlist.isEmpty()) {
            throw new UserNotFoundException("User not found or no wishlist items available for user " + userId);
        }

        // Refresh product details for each item in the wishlist
        for (WishlistItem item : wishlist) {
            try {
                WishlistItem productDetails = fetchProductDetails(item.getProductId());

                // Update product details if they were successfully fetched
                if (productDetails.getName() != null) {
                    item.setName(productDetails.getName());
                    item.setBrand(productDetails.getBrand());
                    item.setPrice(productDetails.getPrice());
                    item.setDiscountedPrice(productDetails.getDiscountedPrice());
                    item.setImageUrl(productDetails.getImageUrl());
                    item.setRating(productDetails.getRating());
                    item.setReviewCount(productDetails.getReviewCount());
                }
            } catch (Exception e) {
                // If there's an error fetching product details, continue with the next item
                continue;
            }
        }

        return wishlist;
    }

    @Transactional
    public void removeItem(Long userId, Long productId) {
        // Check if user has any wishlist items
        List<WishlistItem> userWishlist = repository.findByUserId(userId);
        if (userWishlist.isEmpty()) {
            throw new UserNotFoundException("User not found or no wishlist items available for user " + userId);
        }

        // Check if the specific product is in the user's wishlist
        repository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new ItemNotInWishlistException("Product " + productId + " not found in wishlist for user " + userId));

        repository.deleteByUserIdAndProductId(userId, productId);
    }

    public boolean isInWishlist(Long userId, Long productId) {
        // Check if user has any wishlist items
        List<WishlistItem> userWishlist = repository.findByUserId(userId);
        if (userWishlist.isEmpty()) {
            throw new UserNotFoundException("User not found or no wishlist items available for user " + userId);
        }

        // Check if the specific product is in the user's wishlist
        boolean isPresent = repository.findByUserIdAndProductId(userId, productId).isPresent();
        if (!isPresent) {
            throw new ItemNotInWishlistException("Product " + productId + " not found in wishlist for user " + userId);
        }

        return true;
    }
}
