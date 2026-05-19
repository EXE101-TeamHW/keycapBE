package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.exception.BadRequestException;
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
}

