    package com.example.ecommerce.controller;

    import com.example.ecommerce.exception.CartItemException;
    import com.example.ecommerce.exception.UserException;
    import com.example.ecommerce.model.CartItem;
    import com.example.ecommerce.model.User;
    import com.example.ecommerce.service.CartItemService;
    import com.example.ecommerce.service.UserService;
    import io.swagger.v3.oas.annotations.Operation;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import com.example.ecommerce.config.ApiResponse;

    @RestController
    public class CartItemController {
        @Autowired
        private CartItemService cartItemService;

        @Autowired
        private UserService userService;

        @DeleteMapping("/api/cart_items/{cartItemId}")
        @Operation(description = "Remove Cart Item from Cart")
        @io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Delete Item")
        public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable Long cartItemId, @RequestHeader("Authorization") String jwt) throws UserException, CartItemException {
            User user = userService.findUserProfileByJwt(jwt);
            cartItemService.removeCartItem(user.getId(), cartItemId);
            ApiResponse res = new ApiResponse();
            res.setMessage("Item Removed");
            res.setStatus(true);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }

    //    @PutMapping("/api/cart_items/{cartItemId}")
    //    @Operation(description = "Update Cart Item from Cart")
    //    @io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Update Item")
    //    public ResponseEntity<ApiResponse> updateCartItem(@PathVariable Long cartItemId, @RequestHeader("Authorization") String jwt) throws UserException, CartItemException {
    //        User user = userService.findUserProfileByJwt(jwt);
    //        cartItemService.updateCartItem(user.getId(), cartItemId);
    //        ApiResponse res = new ApiResponse();
    //        res.setMessage("Item Updated");
    //        res.setStatus(true);
    //        return new ResponseEntity<>(res, HttpStatus.OK);
    //
    //    }

        @PutMapping("/api/cart_items/{cartItemId}")
        @Operation(description = "Update Cart Item in Cart")
        @io.swagger.v3.oas.annotations.responses.ApiResponse(description = "Updated Cart Item")
        public ResponseEntity<ApiResponse> updateCartItem(@PathVariable Long cartItemId, @RequestBody CartItem updatedCartItem, @RequestHeader("Authorization") String jwt) throws UserException, CartItemException {
            User user = userService.findUserProfileByJwt(jwt);
            cartItemService.updateCartItem(user.getId(), cartItemId, updatedCartItem);
            ApiResponse res = new ApiResponse();
            res.setMessage("Item Updated");

            res.setStatus(true);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }

    }
