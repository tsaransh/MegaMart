package com.web.megamart.payload;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class AddReview {

    private String feedbackMessage;
    private String reviewBy;
    private List<String> feedBackImageUrls;
    private String productId;

}
