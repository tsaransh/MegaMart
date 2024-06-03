package com.web.megamart.payload;

import lombok.Data;

import java.util.List;

@Data
public class UpdateReview {

    private String reviewId;
    private String feedbackMessage;
    private String reviewBy;
    private List<String> feedBackImageUrls;
    private String productId;

}
