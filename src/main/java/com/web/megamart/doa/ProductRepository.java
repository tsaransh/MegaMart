package com.web.megamart.doa;

import com.web.megamart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {

    Optional<List<Product>> findByProductRegisterBy(String userId);

    @Query(value = "SELECT * FROM product_detail WHERE product_name LIKE %:keyword%", nativeQuery = true)
    Optional<List<Product>> findByKeyword(@Param("keyword") String keyword);
}
