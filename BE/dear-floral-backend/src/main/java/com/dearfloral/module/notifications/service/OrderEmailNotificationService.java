package com.dearfloral.module.notifications.service;

import jakarta.mail.internet.InternetAddress;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderEmailNotificationService {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final JavaMailSender mailSender;
    private final boolean mailEnabled;
    private final String mailFromName;
    private final String mailFromAddress;
    private final String frontendOrderUrl;

    public OrderEmailNotificationService(
            JavaMailSender mailSender,
            @Value("${app.mail.enabled:false}") boolean mailEnabled,
            @Value("${app.mail.from-name:Dear Floral}") String mailFromName,
            @Value("${spring.mail.username:}") String mailFromAddress,
            @Value("${app.mail.frontend-order-url:http://localhost:5173/account/orders}") String frontendOrderUrl
    ) {
        this.mailSender = mailSender;
        this.mailEnabled = mailEnabled;
        this.mailFromName = mailFromName;
        this.mailFromAddress = mailFromAddress;
        this.frontendOrderUrl = frontendOrderUrl;
    }

    @Async
    public void sendAvailableOrderStepEmail(
            String customerEmail,
            String customerName,
            String orderCode,
            String previousStep,
            String currentStep,
            String paymentStatus,
            BigDecimal totalAmount,
            String reason
    ) {
        sendOrderEmail(customerEmail, customerName, "Đơn hàng có sẵn", orderCode, previousStep, currentStep, paymentStatus, totalAmount, reason);
    }

    @Async
    public void sendCustomOrderStepEmail(
            String customerEmail,
            String customerName,
            String orderCode,
            String previousStep,
            String currentStep,
            String paymentStatus,
            BigDecimal totalAmount,
            String reason
    ) {
        sendOrderEmail(customerEmail, customerName, "Đơn hàng custom", orderCode, previousStep, currentStep, paymentStatus, totalAmount, reason);
    }

    private void sendOrderEmail(
            String customerEmail,
            String customerName,
            String orderType,
            String orderCode,
            String previousStep,
            String currentStep,
            String paymentStatus,
            BigDecimal totalAmount,
            String reason
    ) {
        if (!mailEnabled || isBlank(customerEmail)) {
            return;
        }
        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, "UTF-8");
            if (!isBlank(mailFromAddress)) {
                helper.setFrom(new InternetAddress(mailFromAddress, mailFromName, StandardCharsets.UTF_8.name()));
            }
            helper.setTo(customerEmail);
            helper.setSubject("[Dear Floral] Cập nhật trạng thái " + orderCode);
            helper.setText(buildHtml(customerName, orderType, orderCode, previousStep, currentStep, paymentStatus, totalAmount, reason), true);
            mailSender.send(message);
        } catch (Exception ex) {
            log.error("Failed to send order email: orderCode={}, email={}", orderCode, customerEmail, ex);
        }
    }

    private String buildHtml(
            String customerName,
            String orderType,
            String orderCode,
            String previousStep,
            String currentStep,
            String paymentStatus,
            BigDecimal totalAmount,
            String reason
    ) {
        String safeName = isBlank(customerName) ? "Quý khách" : customerName;
        String safeReason = isBlank(reason) ? "-" : reason;
        String safePayment = isBlank(paymentStatus) ? "-" : toVietnamesePaymentStatus(paymentStatus);
        String amountText = totalAmount == null ? "-" : totalAmount.toPlainString() + " VND";
        String at = LocalDateTime.now().format(DATE_TIME_FORMAT);
        String prev = isBlank(previousStep) ? "-" : toVietnameseOrderStep(previousStep);
        String curr = isBlank(currentStep) ? "-" : toVietnameseOrderStep(currentStep);

        return """
                <div style="font-family:Arial,sans-serif;max-width:700px;margin:0 auto;border:1px solid #e5e7eb;border-radius:12px;overflow:hidden">
                  <div style="background:#0f766e;color:#fff;padding:16px 20px">
                    <h2 style="margin:0;font-size:20px;">%s - Thông báo cập nhật đơn hàng</h2>
                    <p style="margin:8px 0 0 0;font-size:13px;opacity:.95;">Mã đơn: <strong>%s</strong></p>
                  </div>
                  <div style="padding:20px;color:#111827;line-height:1.6">
                    <p>Kính gửi <strong>%s</strong>,</p>
                    <p>Đơn hàng của bạn vừa được cập nhật. Chi tiết như sau:</p>
                    <table style="width:100%%;border-collapse:collapse;margin:12px 0 18px 0;font-size:14px;">
                      <tr><td style="padding:8px;border:1px solid #e5e7eb;width:40%%;"><strong>Loại đơn</strong></td><td style="padding:8px;border:1px solid #e5e7eb;">%s</td></tr>
                      <tr><td style="padding:8px;border:1px solid #e5e7eb;"><strong>Bước trước đó</strong></td><td style="padding:8px;border:1px solid #e5e7eb;">%s</td></tr>
                      <tr><td style="padding:8px;border:1px solid #e5e7eb;"><strong>Bước hiện tại</strong></td><td style="padding:8px;border:1px solid #e5e7eb;">%s</td></tr>
                      <tr><td style="padding:8px;border:1px solid #e5e7eb;"><strong>Trạng thái thanh toán</strong></td><td style="padding:8px;border:1px solid #e5e7eb;">%s</td></tr>
                      <tr><td style="padding:8px;border:1px solid #e5e7eb;"><strong>Tổng giá trị đơn</strong></td><td style="padding:8px;border:1px solid #e5e7eb;">%s</td></tr>
                      <tr><td style="padding:8px;border:1px solid #e5e7eb;"><strong>Ghi chú</strong></td><td style="padding:8px;border:1px solid #e5e7eb;">%s</td></tr>
                      <tr><td style="padding:8px;border:1px solid #e5e7eb;"><strong>Thời gian cập nhật</strong></td><td style="padding:8px;border:1px solid #e5e7eb;">%s</td></tr>
                    </table>
                    <p>Bạn có thể theo dõi chi tiết tại: <a href="%s">%s</a></p>
                    <p>Nếu cần hỗ trợ thêm, vui lòng phản hồi email này hoặc liên hệ Dear Floral.</p>
                    <p>Trân trọng,<br/><strong>%s</strong></p>
                  </div>
                </div>
                """.formatted(
                mailFromName,
                orderCode,
                safeName,
                orderType,
                prev,
                curr,
                safePayment,
                amountText,
                safeReason,
                at,
                frontendOrderUrl,
                frontendOrderUrl,
                mailFromName
        );
    }

    private String toVietnameseOrderStep(String step) {
        return switch (step) {
            case "RECEIVED" -> "Đã tiếp nhận";
            case "PROCESSING" -> "Đang xử lý";
            case "SHIPPING" -> "Đang giao hàng";
            case "COMPLETED" -> "Hoàn thành";
            case "CANCELED" -> "Đã hủy";
            case "PENDING_DEPOSIT" -> "Chờ đặt cọc";
            case "PENDING_DEPOSIT_VERIFICATION" -> "Chờ xác nhận đặt cọc";
            case "DEPOSITED" -> "Đã đặt cọc";
            case "WAITING_FLOWER_REVIEW" -> "Chờ đánh giá hoa";
            case "WAITING_FLOWER_RECEIPT" -> "Chờ nhận hoa từ khách hàng";
            case "IN_PROGRESS" -> "Đang thực hiện";
            case "WAITING_DEMO_FEEDBACK" -> "Chờ phản hồi demo";
            case "WAITING_REMAINING_PAYMENT" -> "Chờ thanh toán phần còn lại";
            case "WAITING_REMAINING_PAYMENT_VERIFICATION" -> "Chờ xác nhận thanh toán phần còn lại";
            case "DELIVERING" -> "Đang giao hàng";
            case "WAITING_REFUND_INFO" -> "Chờ thông tin hoàn tiền";
            case "WAITING_REFUND" -> "Chờ hoàn tiền";
            case "REFUNDED" -> "Đã hoàn tiền";
            default -> step;
        };
    }

    private String toVietnamesePaymentStatus(String status) {
        return switch (status) {
            case "UNPAID" -> "Chưa thanh toán";
            case "PENDING" -> "Chờ xác nhận thanh toán";
            case "PAID" -> "Đã thanh toán";
            case "FAILED" -> "Thanh toán thất bại";
            case "PARTIALLY_PAID" -> "Đã thanh toán một phần";
            case "REFUNDED" -> "Đã hoàn tiền";
            default -> status;
        };
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
