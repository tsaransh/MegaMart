package com.web.megamart.services;

import com.web.megamart.entity.Product;
import com.web.megamart.payload.AddProduct;
import com.web.megamart.payload.ProductResponse;
import com.web.megamart.payload.UpdateProduct;

import java.util.List;

public interface ProductService {

    public ProductResponse addProduct(AddProduct product);
    public ProductResponse updateProduct(UpdateProduct product);
    public boolean deleteProduct(String productId, String currentUserUid);
    public ProductResponse getProduct(String productId);
    public List<ProductResponse> getAllProduct();
    public List<ProductResponse> searchProductByKeyword(String keyword);

    public List<ProductResponse> getProductByUserId(String userId);
    public boolean deleteProductByUserId(String userId);

    Product getProductById(String productId);
}
