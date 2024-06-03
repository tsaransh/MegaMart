package com.web.megamart.payload;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ReviewResponse {

    private String reviewId;
    private String feedbackMessage;
    private String reviewBy;
    private Date reviewDate;
    private List<String> feedBackImageUrls;

}
