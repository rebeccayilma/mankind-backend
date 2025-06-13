package com.mankind.matrix_payment_service.controller;

import com.mankind.matrix_payment_service.dto.PaymentIntentResponseDto;
import com.mankind.matrix_payment_service.dto.PaymentRequestDto;
import com.mankind.matrix_payment_service.dto.PaymentResponseDto;
import com.mankind.matrix_payment_service.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "User Payment API", description = "API for user payment operations")
@SecurityRequirement(name = "bearerAuth")
public class UserPaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create a payment intent", description = "Creates a Stripe payment intent and returns client_secret")
    public ResponseEntity<PaymentIntentResponseDto> createPaymentIntent(
            @Valid @RequestBody PaymentRequestDto paymentRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        // Extract user ID from authenticated user
        String userId = userDetails.getUsername();
        
        PaymentIntentResponseDto response = paymentService.createPaymentIntent(paymentRequestDto, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/status/{paymentId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get payment status", description = "Retrieves the status of a specific payment (only if it belongs to logged-in user)")
    public ResponseEntity<PaymentResponseDto> getPaymentStatus(
            @PathVariable UUID paymentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        // Extract user ID from authenticated user
        String userId = userDetails.getUsername();
        
        PaymentResponseDto payment = paymentService.getPaymentByIdAndUserId(paymentId, userId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/my-payments")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get user payments", description = "Retrieves all payments made by the logged-in user")
    public ResponseEntity<List<PaymentResponseDto>> getUserPayments(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        // Extract user ID from authenticated user
        String userId = userDetails.getUsername();
        
        List<PaymentResponseDto> payments = paymentService.getPaymentsByUserId(userId);
        return ResponseEntity.ok(payments);
    }
}