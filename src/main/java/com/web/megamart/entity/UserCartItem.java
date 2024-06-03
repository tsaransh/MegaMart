package com.web.megamart.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "user_cart_item")
public class UserCartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String userUid;

    @OneToMany(mappedBy = "userCartItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @ElementCollection
    private List<Product> products;

}
