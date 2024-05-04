package com.example.ecommerce.service;

import com.example.ecommerce.Request.AddItemRequest;
import com.example.ecommerce.exception.ProductException;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.User;

public interface CartService {
    public Cart createCart(User user);
    public String addCartItem(Long userId, AddItemRequest req) throws ProductException;
    public Cart findUserCart(Long userId) throws ProductException;
}
