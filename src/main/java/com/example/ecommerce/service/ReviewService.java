package com.example.ecommerce.service;

import com.example.ecommerce.Request.ReviewRequest;
import com.example.ecommerce.exception.ProductException;
import com.example.ecommerce.model.Review;
import com.example.ecommerce.model.User;

import java.util.List;

public interface ReviewService {
    public Review createReview(ReviewRequest req, User user) throws ProductException;
    public List<Review> getAllReviews(Long productId);
}
