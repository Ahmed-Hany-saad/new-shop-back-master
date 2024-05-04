//package com.example.ecommerce.controller;
//
//import com.example.ecommerce.config.ApiResponse;
//import com.example.ecommerce.exception.OrderException;
//import com.example.ecommerce.model.Order;
//import com.example.ecommerce.repository.OrderRepository;
//import com.example.ecommerce.responde.PaymentLinkResponse;
//import com.example.ecommerce.service.OrderService;
//import com.example.ecommerce.service.UserService;
//import com.razorpay.Payment;
//import com.razorpay.PaymentLink;
//import com.razorpay.RazorpayClient;
//import com.razorpay.RazorpayException;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api")
//public class PaymentController {
//
//    @Value("${razorpay.api.key")
//    String apiKey;
//
//    @Value("${razorpay.api.secret")
//    String apiSecret;
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//
//    @PostMapping("/payments/{orderId}")
//    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderException, RazorpayException {
//        Order order = orderService.findOrderById(orderId);
//
//        try {
//            RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
//
//            JSONObject paymentLinkRequest = new JSONObject();
//
//            paymentLinkRequest.put("amount", order.getTotalPrice() * 100);
//            paymentLinkRequest.put("currency", "usd");
//
//            JSONObject customer = new JSONObject();
//            customer.put("name", order.getUser().getFirstName());
//            customer.put("email", order.getUser().getEmail());
//            paymentLinkRequest.put("customer", customer);
//
//            JSONObject notify = new JSONObject();
//            notify.put("sms", true);
//            notify.put("email", true);
//            paymentLinkRequest.put("notify", notify);
//
//            paymentLinkRequest.put("callback_url", "http://localhost:3000/payments/"+orderId);
//            paymentLinkRequest.put("callback_method", "get");
//
//            PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);
//
//            String paymentLinkId = payment.get("id");
//            String paymentLinkUrl = payment.get("short_url");
//
//            PaymentLinkResponse response = new PaymentLinkResponse();
//            response.setPayment_link_id(paymentLinkId);
//            response.setPayment_link_url(paymentLinkUrl);
//
//            return new ResponseEntity<PaymentLinkResponse>(response, HttpStatus.CREATED);
//
//
//        } catch (Exception error) {
//            throw new RazorpayException(error.getMessage());
//        }
//
//
//
//
//    }
//
//    @GetMapping("/payments")
//    public ResponseEntity<ApiResponse> redirect(@RequestParam(name="payment_id") String paymentId, @RequestParam(name="order_id") Long orderId) throws OrderException, RazorpayException {
//        Order order = orderService.findOrderById(orderId);
//        RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
//        try {
//            Payment payment = razorpay.payments.fetch(paymentId);
//            if(payment.get("status").equals("captured")) {
//                order.getPaymentDetails().setPaymentId(paymentId);
//                order.getPaymentDetails().setStatus("COMPLETED");
//                order.setOrderStatus("PLACED");
//                orderRepository.save(order);
//
//            }
//            ApiResponse response = new ApiResponse();
//            response.setMessage("Your order get placed");
//            response.setStatus(true);
//            return new ResponseEntity<ApiResponse>(response, HttpStatus.ACCEPTED);
//        } catch (Exception error) {
//            throw new RazorpayException(error.getMessage());
//        }
//
//    }
//}

package com.example.ecommerce.controller;

import com.example.ecommerce.config.ApiResponse;
import com.example.ecommerce.exception.OrderException;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.responde.PaymentLinkResponse;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Value("${stripe.secret.key}")
    String apiKey;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepository orderRepository;

//    @PostMapping("/payments/{orderId}")
//    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderException {
//        Order order = orderService.findOrderById(orderId);
//
//        Stripe.apiKey = apiKey;
//
//        try {
//            PaymentIntentCreateParams params = new PaymentIntentCreateParams.Builder()
//                    .setCurrency("usd")
//                    .setAmount((long) (order.getTotalPrice() * 100)) // amount in cents
//                    .addPaymentMethodType("card")
//                    .setReceiptEmail(order.getUser().getEmail())
//                    .build();
//
//            PaymentIntent paymentIntent = PaymentIntent.create(params);
//
//            PaymentLinkResponse response = new PaymentLinkResponse();
//            response.setPayment_link_id(paymentIntent.getId());
//            response.setPayment_link_url(paymentIntent.getClientSecret()); // Using client_secret as payment link URL
//
//            return new ResponseEntity<>(response, HttpStatus.CREATED);
//
//        } catch (StripeException e) {
//            // Handle StripeException appropriately
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }

    @PostMapping("/payments/{orderId}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws StripeException, OrderException {
        Order order = orderService.findOrderById(orderId);

        Stripe.apiKey = apiKey;

        // Build SessionCreateParams
        SessionCreateParams.Builder builder = new SessionCreateParams.Builder();
        builder.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD);
        builder.setMode(SessionCreateParams.Mode.PAYMENT);
        builder.setSuccessUrl("http://localhost:3000/payments/" + orderId);
        builder.setCancelUrl("http://localhost:3000/payments/fail"); // Optional: Set cancel URL
        builder.addLineItem(SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("usd")
                        .setUnitAmount((long) order.getTotalPrice() * 100)
                        .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName("Payment Request By: " + order.getUser().getFirstName() + " " + order.getUser().getLastName())
                                .build())
                        .build())
                .build());

        // Create Session
        Session session = Session.create(builder.build());

        // Construct PaymentLinkResponse
        PaymentLinkResponse response = new PaymentLinkResponse();
        response.setPayment_link_id(session.getId());
        response.setPayment_link_url(session.getUrl());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/payments")
    public ResponseEntity<ApiResponse> redirect(@RequestParam(name="payment_id") String paymentId, @RequestParam(name="order_id") Long orderId) throws OrderException {
        Order order = orderService.findOrderById(orderId);
        Stripe.apiKey = apiKey;

        try {

            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentId);
            System.out.println(paymentIntent.getStatus());
            if ("succeeded".equals(paymentIntent.getStatus())) {
                order.getPaymentDetails().setPaymentId(paymentId);
                order.getPaymentDetails().setStatus("COMPLETED");
                order.setOrderStatus("PLACED");
                orderRepository.save(order);
            }

            ApiResponse response = new ApiResponse();
            response.setMessage("Your order has been placed");
            response.setStatus(true);
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } catch (StripeException error) {
            // Handle Stripe API errors
            throw new OrderException(error.getMessage());
        }
    }
}
