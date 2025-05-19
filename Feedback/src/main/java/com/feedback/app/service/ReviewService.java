package com.feedback.app.service;

import com.feedback.app.model.Review;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Review operations.
 */
public interface ReviewService {
    
    /**
     * Get all reviews
     * @return List of all reviews
     */
    List<Review> getAllReviews();
    
    /**
     * Get review by ID
     * @param id Review ID
     * @return Optional containing review if found
     */
    Optional<Review> getReviewById(String id);
    
    /**
     * Get reviews by user ID
     * @param userId User ID
     * @return List of reviews by the user
     */
    List<Review> getReviewsByUserId(String userId);
    
    /**
     * Save a review
     * @param review Review to save
     * @return Saved review
     */
    Review saveReview(Review review);
    
    /**
     * Update a review
     * @param id Review ID
     * @param review Updated review data
     * @return Updated review
     */
    Optional<Review> updateReview(String id, Review review);
    
    /**
     * Delete a review
     * @param id Review ID
     * @return true if deleted, false otherwise
     */
    boolean deleteReview(String id);
}
