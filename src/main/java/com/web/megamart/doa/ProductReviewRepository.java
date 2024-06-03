package com.web.megamart.doa;

import com.web.megamart.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepository extends JpaRepository<ProductReview, String> {
}
