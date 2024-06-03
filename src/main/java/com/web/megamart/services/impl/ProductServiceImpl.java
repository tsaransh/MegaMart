package com.web.megamart.services.impl;

import com.web.megamart.doa.ProductRepository;
import com.web.megamart.entity.*;
import com.web.megamart.entity.helper.AccountType;
import com.web.megamart.exception.ProductException;
import com.web.megamart.exception.UserException;
import com.web.megamart.payload.*;
import com.web.megamart.services.InventoryService;
import com.web.megamart.services.MailService;
import com.web.megamart.services.ProductService;
import com.web.megamart.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final MailService mailService;
    private final UserService userService;
    private final InventoryService inventoryService;


    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, MailService mailService, UserService userService, InventoryService inventoryService) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.mailService = mailService;
        this.userService = userService;
        this.inventoryService = inventoryService;
    }

    @Override
    public ProductResponse addProduct(AddProduct addProduct) {

        String userId = addProduct.getProductRegisterBy();
        UserDetail userDetail = userService.findByUserUid(userId);

        if(userDetail.getAccountType().compareTo(AccountType.SELLER.toString())==0 ||
            userDetail.getRole().compareTo("ADMIN") == 0) {

            Product product = modelMapper.map(addProduct, Product.class);
            product.setProductIsInStock(true);
            Product savedProduct = productRepository.save(product);

            Inventory inventory = new Inventory();
            inventory.setProduct(savedProduct);
            inventory.setStockQuantity(addProduct.getQuantity());
            Inventory savedInventory = inventoryService.addItem(inventory);

            savedProduct.setInventory(savedInventory);

            MailData mailData = new MailData();
            mailData.setSendTo(userDetail.getEmail());
            mailData.setSubject("Product Added");
            String message = "Congratulations your Product <b>"
                    +product.getProductName()+
                    "</b> with quantity "
                    +inventory.getStockQuantity()+
                    " was successfully added on Mega Mart.<br><br>" +
                    "for any query contact us on tyagisaransh90@gmail.com" +
                    " <br><br>Team<br>Mega Mart";
            mailData.setMessage(message);
            mailService.sendMail(mailData);

            return mappedToProductResponse(productRepository.save(savedProduct));
        }
        else {
            throw new UserException("Please switch you account to SELLER", HttpStatus.BAD_REQUEST);
        }

    }

    private ProductResponse mappedToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();

        // Mapped to Product Response
        response.setProductId(product.getProductId());
        response.setProductName(product.getProductName());
        response.setProductAmount(product.getProductAmount());
        response.setProductImageUrl(product.getProductImageUrl());
        response.setProductRegisterBy(product.getProductRegisterBy());
        response.setProductRegisterDate(product.getProductRegisterDate());

        if(product.getProductReviews()!=null) {
            List<ProductReview> productReview = product.getProductReviews();
            List<ReviewResponse> reviewResponse = productReview.stream()
                    .map(p -> modelMapper.map(p, ReviewResponse.class))
                    .collect(Collectors.toList());
            response.setProductReviews(reviewResponse);
        }

        GetProductInventory productInventory = new GetProductInventory();
        Inventory inventory = product.getInventory();
        productInventory.setStockQuantity(inventory.getStockQuantity());
        productInventory.setCreateDate(inventory.getCreateDate());
        productInventory.setLastUpdate(inventory.getUpdateDate());
        response.setProductInventory(productInventory);

        return response;
    }

    @Override
    public ProductResponse updateProduct(UpdateProduct product) {
        Product savedProduct = productRepository.findById(product.getProductId())
                .orElseThrow(()-> new ProductException("Product not found", HttpStatus.NOT_FOUND));

        savedProduct.setProductName(product.getProductName());
        savedProduct.setProductAmount(product.getProductAmount());
        savedProduct.setProductImageUrl(product.getProductImageUrl());

        Inventory inventory = savedProduct.getInventory();
        if(inventory.getStockQuantity() < product.getQuantity()) {
            int newQuantity = inventory.getStockQuantity() + product.getQuantity();
            inventory.setStockQuantity(newQuantity);
        } else {
            int newQuantity = inventory.getStockQuantity() - product.getQuantity();
            inventory.setStockQuantity(newQuantity);
        }
        Inventory savedInventory = inventoryService.update(inventory);
        savedProduct.setInventory(savedInventory);

        Product newSavedProduct = productRepository.save(savedProduct);

        return mappedToProductResponse(newSavedProduct);
    }

    @Override
    public boolean deleteProduct(String productId, String currentUserUid) {

        ProductResponse productResponse = getProduct(productId);
        String userUid = productResponse.getProductRegisterBy();
        UserDetail userDetail = userService.findByUserUid(userUid);

        if(userDetail.getId().compareTo(currentUserUid) != 0) {
            throw new ProductException("You don't have right to delete this product", HttpStatus.BAD_REQUEST);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException( "Product not found", HttpStatus.NOT_FOUND));

        productRepository.delete(product);

        return true;
    }

    @Override
    public ProductResponse getProduct(String productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException( "Product not found", HttpStatus.NOT_FOUND));

        return mappedToProductResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProduct() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(product -> mappedToProductResponse(product))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> searchProductByKeyword(String keyword) {

        List<Product> products = productRepository.findByKeyword(keyword)
                .orElseThrow(() ->  new ProductException("No Product Found", HttpStatus.NOT_FOUND));

        return products.stream().map((product) -> mappedToProductResponse(product))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductByUserId(String userId) {

        UserDetail userDetail = userService.findByUserUid(userId);
        if(userDetail.getAccountType().compareTo(AccountType.SELLER.toString())==0
                || userDetail.getRole().compareTo("ADMIN") == 0) {

            List<Product> products = productRepository.findByProductRegisterBy(userId)
                    .orElseThrow(() -> new ProductException("No Product Found", HttpStatus.NOT_FOUND));

            return products.stream().map((product) -> mappedToProductResponse(product))
                    .collect(Collectors.toList());
        }
        else {
            throw new UserException("Please switch you account to SELLER", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public boolean deleteProductByUserId(String userId) {
        UserDetail userDetail = userService.findByUserUid(userId);
        if(userDetail.getAccountType().compareTo("SELLER") == 0
                || userDetail.getRole().compareTo("ADMIN") == 0) {
            List<Product> products = productRepository.findByProductRegisterBy(userId)
                    .orElseThrow(() -> new ProductException("No Product Found", HttpStatus.NOT_FOUND));
            products.forEach(product -> productRepository.delete(product));
            return true;
        }
        return false;
    }

    @Override
    public Product getProductById(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException( "Product not found", HttpStatus.NOT_FOUND));
        return product;
    }
}
