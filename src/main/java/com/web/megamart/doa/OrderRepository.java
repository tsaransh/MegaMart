package com.web.megamart.doa;

import com.web.megamart.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    Optional<List<Order>> findByOrderTo(String userUid);
    Optional<List<Order>> findByOrderBy(String userUid);
}
