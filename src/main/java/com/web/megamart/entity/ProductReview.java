package com.web.megamart.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "product_review")
@EntityListeners(AuditingEntityListener.class)
public class ProductReview {

    @Id
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", nullable = false, updatable = false)
    private String reviewId;

    @NotNull
    private String feedbackMessage;

    @NotNull
    private String reviewBy;

    @CreatedDate
    private Date reviewDate;

    @ElementCollection
    private List<String> feedBackImageUrls;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
