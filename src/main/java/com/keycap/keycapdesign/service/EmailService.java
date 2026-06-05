package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.exception.BadRequestException;
import com.keycap.keycapdesign.entity.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${mail.enabled:true}")
    private boolean mailEnabled;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationCode(String toEmail, String code) {
        if (!mailEnabled) {
            return;
        }
        if (fromEmail == null || fromEmail.isBlank()) {
            throw new BadRequestException("Mail sender is not configured");
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Verify your KeycapDesign account");
            helper.setText(buildHtml(code), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new BadRequestException("Failed to send verification email");
        }
    }

    private String buildHtml(String code) {
        String template = """
                <div style="font-family:Arial,sans-serif;background:#f6f7fb;padding:24px;">
                  <div style="max-width:560px;margin:auto;background:#ffffff;border-radius:12px;padding:24px;">
                    <h2 style="margin:0 0 12px;color:#1f2a44;">KeycapDesign</h2>
                    <p style="margin:0 0 12px;color:#3b4252;">Thanks for signing up. Use the code below to verify your email:</p>
                    <div style="font-size:28px;font-weight:700;letter-spacing:4px;background:#eef2ff;color:#3b5bdb;
                      padding:12px 16px;border-radius:10px;text-align:center;">%s</div>
                    <p style="margin:12px 0 0;color:#6b7280;font-size:13px;">This code expires in 10 minutes.</p>
                    <hr style="border:none;border-top:1px solid #e5e7eb;margin:16px 0;">
                    <p style="margin:0;color:#9aa0a6;font-size:12px;">If you did not request this, you can ignore this email.</p>
                  </div>
                </div>
                """;
        return String.format(template, code);
    }

    public void sendForgotPasswordCode(String toEmail, String code) {
        if (!mailEnabled) {
            return;
        }
        if (fromEmail == null || fromEmail.isBlank()) {
            throw new BadRequestException("Mail sender is not configured");
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Reset your KeycapDesign password");
            helper.setText(buildForgotPasswordHtml(code), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new BadRequestException("Failed to send password reset email");
        }
    }

    public void sendCustomOrderCreated(Order order) {
        if (!mailEnabled) {
            return;
        }
        if (order == null || order.getUser() == null || order.getUser().getEmail() == null
                || order.getUser().getEmail().isBlank()) {
            return;
        }
        if (fromEmail == null || fromEmail.isBlank()) {
            throw new BadRequestException("Mail sender is not configured");
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(order.getUser().getEmail());
            helper.setSubject("Đơn custom của bạn đã được lên đơn");
            helper.setText(buildCustomOrderCreatedHtml(order), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new BadRequestException("Failed to send custom order email");
        }
    }

    private String buildCustomOrderCreatedHtml(Order order) {
        String customerName = order.getUser().getFullName() != null && !order.getUser().getFullName().isBlank()
                ? order.getUser().getFullName()
                : order.getUser().getEmail();
        String designName = order.getTicket() != null && order.getTicket().getRequest() != null
                ? order.getTicket().getRequest().getDesignName()
                : "Đơn custom";
        String amount = order.getTotalAmount() == null
                ? "Chưa cập nhật"
                : String.format("%,.0fđ", order.getTotalAmount());
        String template = """
                <div style="font-family:Arial,sans-serif;background:#f6f7fb;padding:24px;">
                  <div style="max-width:600px;margin:auto;background:#ffffff;border-radius:12px;padding:24px;">
                    <h2 style="margin:0 0 12px;color:#1f2a44;">HWShop</h2>
                    <p style="margin:0 0 12px;color:#3b4252;">Xin chào %s,</p>
                    <p style="margin:0 0 12px;color:#3b4252;">Đơn custom của bạn đã được staff lên đơn và chuyển sang quy trình xử lý vận chuyển.</p>
                    <div style="background:#f8fafc;border:1px solid #e5e7eb;border-radius:10px;padding:14px;margin:16px 0;">
                      <p style="margin:0 0 8px;color:#111827;"><strong>Mã đơn:</strong> %s</p>
                      <p style="margin:0 0 8px;color:#111827;"><strong>Thiết kế:</strong> %s</p>
                      <p style="margin:0;color:#111827;"><strong>Giá custom:</strong> %s</p>
                    </div>
                    <p style="margin:0;color:#6b7280;font-size:13px;">Bạn có thể theo dõi đơn trong mục Đơn hàng của tôi.</p>
                  </div>
                </div>
                """;
        return String.format(template, customerName, order.getOrderCode(), designName, amount);
    }

    private String buildForgotPasswordHtml(String code) {
        String template = """
                <div style="font-family:Arial,sans-serif;background:#f6f7fb;padding:24px;">
                  <div style="max-width:560px;margin:auto;background:#ffffff;border-radius:12px;padding:24px;">
                    <h2 style="margin:0 0 12px;color:#1f2a44;">KeycapDesign</h2>
                    <p style="margin:0 0 12px;color:#3b4252;">You requested a password reset. Use the code below to reset your password:</p>
                    <div style="font-size:28px;font-weight:700;letter-spacing:4px;background:#fef3c7;color:#d97706;
                      padding:12px 16px;border-radius:10px;text-align:center;">%s</div>
                    <p style="margin:12px 0 0;color:#6b7280;font-size:13px;">This code expires in 10 minutes.</p>
                    <hr style="border:none;border-top:1px solid #e5e7eb;margin:16px 0;">
                    <p style="margin:0;color:#9aa0a6;font-size:12px;">If you did not request this, you can safely ignore this email. Your password will remain unchanged.</p>
                  </div>
                </div>
                """;
        return String.format(template, code);
    }
}

