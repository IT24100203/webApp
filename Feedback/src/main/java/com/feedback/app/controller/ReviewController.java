package com.feedback.app.controller;

import com.feedback.app.model.Review;
import com.feedback.app.model.User;
import com.feedback.app.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller for review-related operations.
 */
@Controller
public class ReviewController {
    
    private final ReviewService reviewService;
    
    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }
    
    /**
     * Redirect root to reviews page
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/reviews";
    }
    
    /**
     * Show all reviews
     */
    @GetMapping("/reviews")
    public String showAllReviews(Model model, HttpSession session) {
        List<Review> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
        
        // Add user to model if logged in
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
        
        return "reviews";
    }
    
    /**
     * Show add review form
     */
    @GetMapping("/reviews/add")
    public String showAddReviewForm(HttpSession session, RedirectAttributes redirectAttributes) {
        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to add a review");
            return "redirect:/login";
        }
        
        return "add-review";
    }
    
    /**
     * Process add review
     */
    @PostMapping("/reviews/add")
    public String addReview(@RequestParam String title,
                            @RequestParam String content,
                            @RequestParam int rating,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        
        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to add a review");
            return "redirect:/login";
        }
        
        // Validate rating
        if (rating < 1 || rating > 5) {
            redirectAttributes.addFlashAttribute("error", "Rating must be between 1 and 5");
            return "redirect:/reviews/add";
        }
        
        // Create new review
        Review review = new Review();
        review.setId(UUID.randomUUID().toString());
        review.setUserId(user.getId());
        review.setUsername(user.getUsername());
        review.setTitle(title);
        review.setContent(content);
        review.setRating(rating);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());
        
        reviewService.saveReview(review);
        
        redirectAttributes.addFlashAttribute("success", "Review added successfully");
        return "redirect:/reviews";
    }
    
    /**
     * Show my reviews
     */
    @GetMapping("/my-reviews")
    public String showMyReviews(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to view your reviews");
            return "redirect:/login";
        }
        
        List<Review> reviews = reviewService.getReviewsByUserId(user.getId());
        model.addAttribute("reviews", reviews);
        model.addAttribute("user", user);
        
        return "my-reviews";
    }
    
    /**
     * Show edit review form
     */
    @GetMapping("/reviews/edit/{id}")
    public String showEditReviewForm(@PathVariable String id,
                                     Model model,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        
        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to edit a review");
            return "redirect:/login";
        }
        
        // Get review
        Optional<Review> reviewOpt = reviewService.getReviewById(id);
        if (reviewOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Review not found");
            return "redirect:/my-reviews";
        }
        
        Review review = reviewOpt.get();
        
        // Check if user is the owner of the review
        if (!review.getUserId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("error", "You can only edit your own reviews");
            return "redirect:/my-reviews";
        }
        
        model.addAttribute("review", review);
        
        return "edit-review";
    }
    
    /**
     * Process edit review
     */
    @PostMapping("/reviews/edit/{id}")
    public String editReview(@PathVariable String id,
                             @RequestParam String title,
                             @RequestParam String content,
                             @RequestParam int rating,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        
        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to edit a review");
            return "redirect:/login";
        }
        
        // Get review
        Optional<Review> reviewOpt = reviewService.getReviewById(id);
        if (reviewOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Review not found");
            return "redirect:/my-reviews";
        }
        
        Review review = reviewOpt.get();
        
        // Check if user is the owner of the review
        if (!review.getUserId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("error", "You can only edit your own reviews");
            return "redirect:/my-reviews";
        }
        
        // Validate rating
        if (rating < 1 || rating > 5) {
            redirectAttributes.addFlashAttribute("error", "Rating must be between 1 and 5");
            return "redirect:/reviews/edit/" + id;
        }
        
        // Update review
        review.setTitle(title);
        review.setContent(content);
        review.setRating(rating);
        review.setUpdatedAt(LocalDateTime.now());
        
        reviewService.updateReview(id, review);
        
        redirectAttributes.addFlashAttribute("success", "Review updated successfully");
        return "redirect:/my-reviews";
    }
    
    /**
     * Delete review
     */
    @GetMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable String id,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        
        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to delete a review");
            return "redirect:/login";
        }
        
        // Get review
        Optional<Review> reviewOpt = reviewService.getReviewById(id);
        if (reviewOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Review not found");
            return "redirect:/my-reviews";
        }
        
        Review review = reviewOpt.get();
        
        // Check if user is the owner of the review
        if (!review.getUserId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("error", "You can only delete your own reviews");
            return "redirect:/my-reviews";
        }
        
        // Delete review
        reviewService.deleteReview(id);
        
        redirectAttributes.addFlashAttribute("success", "Review deleted successfully");
        return "redirect:/my-reviews";
    }
}
