package com.web.megamart.services.impl;

import com.web.megamart.doa.ProductReviewRepository;
import com.web.megamart.entity.Product;
import com.web.megamart.entity.ProductReview;
import com.web.megamart.exception.ProductReviewException;
import com.web.megamart.payload.AddReview;
import com.web.megamart.payload.ReviewResponse;
import com.web.megamart.payload.UpdateReview;
import com.web.megamart.services.ProductReviewService;
import com.web.megamart.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductReviewServiceImpl implements ProductReviewService {

    private final ProductReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final ProductService productService;

    public ProductReviewServiceImpl(ProductReviewRepository reviewRepository, ModelMapper modelMapper, ProductService productService) {
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
        this.productService = productService;
    }

    @Override
    public ReviewResponse addReview(AddReview addReview) {
        ProductReview productReview = modelMapper.map(addReview, ProductReview.class);
        Product product = productService.getProductById(addReview.getProductId());
        productReview.setProduct(product);

        ProductReview savedReview = reviewRepository.save(productReview);

        return modelMapper.map(savedReview, ReviewResponse.class);
    }

    @Override
    public ReviewResponse updateReview(UpdateReview updateReview) {

        ProductReview productReview = getProductReview(updateReview.getReviewId());
        productReview.setFeedbackMessage(updateReview.getFeedbackMessage());
        productReview.setFeedBackImageUrls(updateReview.getFeedBackImageUrls());

        return modelMapper.map(reviewRepository.save(productReview), ReviewResponse.class);
    }

    @Override
    public List<ReviewResponse> getAllReview(String productId) {
//        Optional<List<List>> productList = reviewRepository.findAllBy
        return List.of();
    }

    private ProductReview getProductReview(String reviewId) {
        ProductReview productReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ProductReviewException("Review Not Found with Id: "+reviewId, HttpStatus.NOT_FOUND));
        return productReview;
    }

    @Override
    public ReviewResponse getReview(String reviewId) {
        ProductReview productReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ProductReviewException("Review Not Found with Id: "+reviewId, HttpStatus.NOT_FOUND));
        return modelMapper.map(productReview, ReviewResponse.class);
    }

    @Override
    public boolean deleteReview(String reviewId) {
        return false;
    }

    @Override
    public boolean deleteReviewByProductId(String productId) {
        return false;
    }
}
