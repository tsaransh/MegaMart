package com.web.megamart.services;

import com.web.megamart.payload.AddReview;
import com.web.megamart.payload.ReviewResponse;
import com.web.megamart.payload.UpdateReview;

import java.util.List;

public interface ProductReviewService {

    public ReviewResponse addReview(AddReview addReview);
    public ReviewResponse updateReview(UpdateReview updateReview);
    public List<ReviewResponse> getAllReview(String productId);
    public ReviewResponse getReview(String reviewId);
    public boolean deleteReview(String reviewId);
    public boolean deleteReviewByProductId(String productId);

}
