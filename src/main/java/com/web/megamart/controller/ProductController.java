package com.web.megamart.controller;

import com.web.megamart.entity.Product;
import com.web.megamart.exception.InventoryException;
import com.web.megamart.exception.ProductException;
import com.web.megamart.exception.UserException;
import com.web.megamart.payload.AddProduct;
import com.web.megamart.payload.ProductResponse;
import com.web.megamart.payload.UpdateProduct;
import com.web.megamart.services.ProductService;
import org.hibernate.sql.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<ProductResponse> addProduct(@RequestBody AddProduct addProduct) {
        return ResponseEntity.ok(productService.addProduct(addProduct));
    }

    @PostMapping("/update")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody UpdateProduct addProduct) {
        return ResponseEntity.ok(productService.updateProduct(addProduct));
    }

    @GetMapping("/get")
    public ResponseEntity<ProductResponse> getProductById(@RequestParam String productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProduct(@RequestParam("value") String keyword) {
        return ResponseEntity.ok(productService.searchProductByKeyword(keyword));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ProductResponse>> getAllProduct() {
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteProduct(@RequestParam String productId, @RequestParam String currentUserUid) {
        return ResponseEntity.ok(productService.deleteProduct(productId, currentUserUid));
    }

    @GetMapping("/get-product-by-userid")
    public ResponseEntity<List<ProductResponse>> getProductByUserUid(@RequestParam("userUid") String userUid) {
        return ResponseEntity.ok(productService.getProductByUserId(userUid));
    }

    @DeleteMapping("/delete-product-by-userid")
    public ResponseEntity<Boolean> deleteProductByUserUid(@RequestParam("userUid") String userUid) {
        return ResponseEntity.ok(productService.deleteProductByUserId(userUid));
    }



    @ExceptionHandler(ProductException.class)
    public ResponseEntity<String> handleProductException(ProductException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(exception.getMessage());
    }
    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handleUserException(UserException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
    }
    @ExceptionHandler(InventoryException.class)
    public ResponseEntity<String> handleInventoryException(InventoryException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
    }

}
