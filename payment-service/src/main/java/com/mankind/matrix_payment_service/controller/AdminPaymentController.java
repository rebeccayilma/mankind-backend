package com.mankind.matrix_payment_service.controller;

import com.mankind.matrix_payment_service.dto.PaymentLogResponseDto;
import com.mankind.matrix_payment_service.dto.PaymentResponseDto;
import com.mankind.matrix_payment_service.model.PaymentStatus;
import com.mankind.matrix_payment_service.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/payments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Payment API", description = "API for admin payment operations")
@SecurityRequirement(name = "bearerAuth")
public class AdminPaymentController {

    private final PaymentService paymentService;

    @GetMapping
    @Operation(summary = "Get all payments", description = "Retrieves all payment records")
    public ResponseEntity<List<PaymentResponseDto>> getAllPayments() {
        List<PaymentResponseDto> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{paymentId}")
    @Operation(summary = "Get payment by ID", description = "Retrieves details of a payment by ID")
    public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable UUID paymentId) {
        PaymentResponseDto payment = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get payments by user ID", description = "Retrieves payment history for a specific user")
    public ResponseEntity<List<PaymentResponseDto>> getPaymentsByUserId(@PathVariable String userId) {
        List<PaymentResponseDto> payments = paymentService.getPaymentsByUserId(userId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get payments by status", description = "Retrieves payments with a specific status")
    public ResponseEntity<List<PaymentResponseDto>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        List<PaymentResponseDto> payments = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{paymentId}/logs")
    @Operation(summary = "Get payment logs by payment ID", description = "Retrieves logs for a specific payment")
    public ResponseEntity<List<PaymentLogResponseDto>> getPaymentLogsByPaymentId(@PathVariable UUID paymentId) {
        List<PaymentLogResponseDto> logs = paymentService.getPaymentLogsByPaymentId(paymentId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/logs")
    @Operation(summary = "Get all payment logs", description = "Retrieves all entries from PaymentLog table")
    public ResponseEntity<List<PaymentLogResponseDto>> getAllPaymentLogs() {
        List<PaymentLogResponseDto> logs = paymentService.getAllPaymentLogs();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/logs/date-range")
    @Operation(summary = "Get payment logs by date range", description = "Retrieves logs created between two dates")
    public ResponseEntity<List<PaymentLogResponseDto>> getPaymentLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<PaymentLogResponseDto> logs = paymentService.getPaymentLogsBetweenDates(startDate, endDate);
        return ResponseEntity.ok(logs);
    }
}