package com.example.ecommerce.service;

import com.example.ecommerce.Request.CreateProductRequest;
import com.example.ecommerce.exception.ProductException;
import com.example.ecommerce.model.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    public Product createProduct(CreateProductRequest req);
    public String deleteProduct(Long productid) throws ProductException;
    public Product updateProduct(Long productid, Product req) throws ProductException;
    public Product findProductByld(Long id) throws ProductException;
    public List<Product> findProductByCategory(String category);

    public Page<Product>getAllProduct(String category,List<String>colors,List<String>sizes,Integer minPrice,Integer maxPrice,Integer minDiscount,String sort,String stock,Integer pageNumber,Integer pageSize);
// i edit this
    List<Product> findAllProducts();
}
