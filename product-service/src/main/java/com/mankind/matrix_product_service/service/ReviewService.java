package com.mankind.matrix_product_service.service;

import com.mankind.api.product.dto.review.CreateReviewDTO;
import com.mankind.api.product.dto.review.ReviewDTO;
import com.mankind.matrix_product_service.model.Product;
import com.mankind.matrix_product_service.model.Review;
import com.mankind.matrix_product_service.repository.ProductRepository;
import com.mankind.matrix_product_service.repository.ReviewRepository;
import com.mankind.matrix_product_service.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private UserClient userClient;

    @Transactional
    public ReviewDTO createReview(CreateReviewDTO createReviewDTO) {
        Product product = productRepository.findById(createReviewDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Review review = new Review();
        review.setUserId(currentUserService.getCurrentUserId());
        review.setProduct(product);
        review.setRating(createReviewDTO.getRating());
        review.setComment(createReviewDTO.getComment());

        Review savedReview = reviewRepository.save(review);
        updateProductAverageRating(product.getId());
        return convertToDTO(savedReview);
    }

    public List<ReviewDTO> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsByUserId(Long userId) {
        // Verify user exists
        try {
            userClient.getUserById(userId);
        } catch (Exception e) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return reviewRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDTO updateReview(Long reviewId, CreateReviewDTO createReviewDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // Only allow the current user to update their own review
        Long currentUserId = currentUserService.getCurrentUserId();
        if (!review.getUserId().equals(currentUserId)) {
            throw new RuntimeException("You can only update your own reviews");
        }

        review.setRating(createReviewDTO.getRating());
        review.setComment(createReviewDTO.getComment());

        Review updatedReview = reviewRepository.save(review);
        updateProductAverageRating(review.getProduct().getId());
        return convertToDTO(updatedReview);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        Long currentUserId = currentUserService.getCurrentUserId();
        if (!review.getUserId().equals(currentUserId)) {
            throw new RuntimeException("You can only delete your own reviews");
        }
        Long productId = review.getProduct().getId();
        reviewRepository.deleteById(reviewId);
        updateProductAverageRating(productId);
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setUserId(review.getUserId());
        dto.setProductId(review.getProduct().getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());
        return dto;
    }

    private void updateProductAverageRating(Long productId) {
        List<Review> reviews = reviewRepository.findTop100ByProductIdOrderByCreatedAtDesc(productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (reviews.isEmpty()) {
            product.setAverageRating(null);
        } else {
            double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
            product.setAverageRating(avg);
        }
        productRepository.save(product);
    }
} 