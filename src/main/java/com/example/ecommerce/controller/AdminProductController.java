package com.example.ecommerce.controller;

import com.example.ecommerce.Request.CreateProductRequest;
import com.example.ecommerce.config.ApiResponse;
import com.example.ecommerce.exception.ProductException;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {
    @Autowired
    ProductService productService;
    @PostMapping("/")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest req,@RequestHeader("Authorization") String jwt) {
        Product product = productService.createProduct(req);
        return new ResponseEntity<Product>(product, HttpStatus.CREATED);
    }
    @DeleteMapping("{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable long productId) throws ProductException {
        productService.deleteProduct(productId);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Product deleted successfully");
        apiResponse.setStatus(true);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Product>> findAllProduct() {
        List<Product>products=productService.findAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @PutMapping("/{productId}/update")
    public ResponseEntity<Product>updateProduct (@RequestBody Product product, @PathVariable long productId) throws ProductException {
        productService.updateProduct(productId, product);
        return new ResponseEntity<Product>(product, HttpStatus.CREATED);
    }
    @PostMapping("/creates")
    public ResponseEntity<ApiResponse> createMultipleProduct(@RequestBody CreateProductRequest[] req)  {
        for (CreateProductRequest product:req){
            productService.createProduct(product);
        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Product created successfully");
        apiResponse.setStatus(true);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
}
