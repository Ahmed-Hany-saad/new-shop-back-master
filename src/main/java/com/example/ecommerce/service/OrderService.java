package com.example.ecommerce.service;

import com.example.ecommerce.exception.OrderException;
import com.example.ecommerce.exception.ProductException;
import com.example.ecommerce.model.Address;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;

import java.util.List;

public interface OrderService {

    public Order createOrder(User user, Address address) throws ProductException;
    public Order findOrderById(long orderId) throws OrderException;
    public List<Order> userOrderHistory(long userId);
    public Order placedOrder(Long orderId) throws OrderException;
    public Order confirmedOrder(Long orderId) throws OrderException;
    public Order shippedOrder(Long orderId) throws OrderException;
    public Order deliveredOrder(Long orderId) throws OrderException;
    public Order cancelledOrder(Long orderId) throws OrderException;
    public List<Order> getAllOrders();
    public void deleteOrder(long orderId) throws OrderException;
}
