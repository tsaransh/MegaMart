package com.web.megamart.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "product_detail")
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", nullable = false, updatable = false)
    private String productId;

    @NotNull
    @Column(nullable = false)
    private String productName;

    @NotNull
    @Column(nullable = false)
    private double productAmount;

    @ElementCollection
    @CollectionTable(name = "product_image_urls", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> productImageUrl;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date productRegisterDate;

    @NotNull
    @Column(nullable = false)
    private String productRegisterBy;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductReview> productReviews;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Inventory inventory;

    @Column(nullable = false)
    private boolean productIsInStock;

    @ManyToOne
    @JoinColumn(name = "user_cart_item_id")
    private UserCartItem userCartItem;

}
