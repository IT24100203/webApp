package com.feedback.app.service.impl;

import com.feedback.app.model.Review;
import com.feedback.app.service.ReviewService;
import com.feedback.app.util.FileStorageUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of ReviewService interface.
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    private final FileStorageUtil<Review> fileStorage;

    /**
     * Constructor
     */
    public ReviewServiceImpl() {
        this.fileStorage = new FileStorageUtil<>("data/reviews.dat", Review.class);

        // Add some sample reviews if none exist
        if (getAllReviews().isEmpty()) {
            // Sample review 1
            Review review1 = new Review(
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(), // Random admin user ID
                    "admin",
                    "Great Service",
                    "The service was excellent and exceeded my expectations.",
                    5
            );
            review1.setCreatedAt(LocalDateTime.now().minusDays(5));
            review1.setUpdatedAt(LocalDateTime.now().minusDays(5));
            saveReview(review1);

            // Sample review 2
            Review review2 = new Review(
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(), // Random regular user ID
                    "user",
                    "Good Product",
                    "The product quality is good, but delivery was delayed.",
                    4
            );
            review2.setCreatedAt(LocalDateTime.now().minusDays(3));
            review2.setUpdatedAt(LocalDateTime.now().minusDays(3));
            saveReview(review2);
        }
    }

    @Override
    public List<Review> getAllReviews() {
        return fileStorage.getAll();
    }

    @Override
    public Optional<Review> getReviewById(String id) {
        return fileStorage.findOne(review -> review.getId().equals(id));
    }

    @Override
    public List<Review> getReviewsByUserId(String userId) {
        return fileStorage.findAll(review -> review.getUserId().equals(userId));
    }

    @Override
    public Review saveReview(Review review) {
        // Generate ID if not provided
        if (review.getId() == null || review.getId().isEmpty()) {
            review.setId(UUID.randomUUID().toString());
        }

        // Set timestamps if not provided
        if (review.getCreatedAt() == null) {
            review.setCreatedAt(LocalDateTime.now());
        }
        if (review.getUpdatedAt() == null) {
            review.setUpdatedAt(LocalDateTime.now());
        }

        return fileStorage.save(review);
    }

    @Override
    public Optional<Review> updateReview(String id, Review updatedReview) {
        return getReviewById(id).map(existingReview -> {
            existingReview.setTitle(updatedReview.getTitle());
            existingReview.setContent(updatedReview.getContent());
            existingReview.setRating(updatedReview.getRating());
            existingReview.setUpdatedAt(LocalDateTime.now());

            return fileStorage.update(
                    review -> review.getId().equals(id),
                    existingReview
            ).orElse(null);
        });
    }

    @Override
    public boolean deleteReview(String id) {
        return fileStorage.delete(review -> review.getId().equals(id));
    }
}
