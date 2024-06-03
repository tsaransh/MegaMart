package com.web.megamart.controller;

import com.web.megamart.exception.ProductReviewException;
import com.web.megamart.payload.AddReview;
import com.web.megamart.payload.ReviewResponse;
import com.web.megamart.payload.UpdateReview;
import com.web.megamart.services.ProductReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vi/product/review")
public class ProductReviewController {

    private final ProductReviewService reviewService;

    public ProductReviewController(ProductReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/add")
    public ResponseEntity<ReviewResponse> addReview(@RequestBody AddReview addReview) {
        return ResponseEntity.ok(reviewService.addReview(addReview));
    }

    @PostMapping("/update")
    public ResponseEntity<ReviewResponse> updateReview(@RequestBody UpdateReview updateReview) {
        return ResponseEntity.ok(reviewService.updateReview(updateReview));
    }

    @GetMapping("/get-review")
    public ResponseEntity<ReviewResponse> getReview(@RequestParam String reviewId) {
        return ResponseEntity.ok(reviewService.getReview(reviewId));
    }

    @GetMapping("/get-all-review")
    public ResponseEntity<List<ReviewResponse>> getAllReviewByProductId(@RequestParam String productId) {
        return ResponseEntity.ok(reviewService.getAllReview(productId));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteReview(@RequestParam String reviewId) {
        return ResponseEntity.ok(reviewService.deleteReview(reviewId));
    }

    @DeleteMapping("/delete-all-review")
    public ResponseEntity<Boolean> deleteAllReviewByProductId(@RequestParam String productId) {
        return ResponseEntity.ok(reviewService.deleteReviewByProductId(productId));
    }

    @ExceptionHandler(ProductReviewException.class)
    public ResponseEntity<String> errorHandling(ProductReviewException reviewException) {
        return ResponseEntity.status(reviewException.getHttpStatus()).body(reviewException.getMessage());
    }

}
