package com.mankind.matrix_payment_service.controller;

import com.mankind.matrix_payment_service.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Webhook API", description = "API for handling Stripe webhooks")
public class WebhookController {

    private final PaymentService paymentService;

    @PostMapping("/webhook")
    @Operation(summary = "Handle Stripe webhook", description = "Processes Stripe payment status events")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature) {
        
        log.info("Received webhook from Stripe");
        
        try {
            boolean processed = paymentService.handleStripeWebhook(payload, signature);
            
            if (processed) {
                return ResponseEntity.ok("Webhook processed successfully");
            } else {
                log.warn("Webhook processing failed");
                return ResponseEntity.badRequest().body("Webhook processing failed");
            }
        } catch (Exception e) {
            log.error("Error processing webhook: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error processing webhook: " + e.getMessage());
        }
    }
}