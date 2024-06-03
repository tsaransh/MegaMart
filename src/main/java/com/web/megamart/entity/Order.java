package com.web.megamart.entity;

import com.web.megamart.entity.helper.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

@Data
@Entity
@Table(name = "order_details")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", nullable = false, updatable = false)
    private String orderId;

    private int quantity; // Changed to int

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String orderBy; // Fixed typo

    @CreatedDate
    private Date orderDate;

    private LocalDate deliveryDate;

    private String deliveryAddress;

    @ElementCollection
    @CollectionTable(name = "order_status", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "status")
    @Column(name = "status_date")
    @Enumerated(EnumType.STRING) // Assuming OrderStatus is an enum
    private Map<OrderStatus, Date> orderStatus;

    private boolean completed;

}
