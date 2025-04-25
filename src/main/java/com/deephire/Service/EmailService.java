package com.deephire.Service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String email, String name, String rawPassword) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("amirouni162@gmail.com");
            helper.setTo(email);
            helper.setSubject("Welcome to DeepHire - Your Account is Ready!");

            // HTML email template with login info
            String htmlTemplate = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Welcome to DeepHire</title>
                </head>
                <body style="margin: 0; padding: 0; font-family: Arial, Helvetica, sans-serif; background-color: #f4f4f4;">
                    <table align="center" border="0" cellpadding="0" cellspacing="0" width="100%%" style="max-width: 600px; background-color: #ffffff; margin: 20px auto; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                        <!-- Header -->
                        <tr>
                            <td style="background-color: #007bff; padding: 20px; text-align: center; border-top-left-radius: 8px; border-top-right-radius: 8px;">
                                <h1 style="color: #ffffff; margin: 0; font-size: 24px;">Welcome to DeepHire!</h1>
                            </td>
                        </tr>
                        <!-- Body -->
                        <tr>
                            <td style="padding: 30px;">
                                <p style="color: #333333; font-size: 18px; margin-bottom: 10px;">Hello <strong>%s</strong>,</p>
                                <p style="color: #555555; font-size: 16px; line-height: 1.5; margin-bottom: 20px;">
                                    We're excited to have you join <strong>DeepHire</strong>. Your recruiter account has been successfully created.
                                    Below are your login details. Please keep them safe and change your password after your first login.
                                </p>
                                <table style="width: 100%%; background-color: #f9f9f9; padding: 15px; border-radius: 6px; margin-bottom: 20px;">
                                    <tr>
                                        <td style="font-weight: bold; padding: 5px 0;">Login User name:</td>
                                        <td style="padding: 5px 0;">%s</td>
                                    </tr>
                                    <tr>
                                        <td style="font-weight: bold; padding: 5px 0;">Password:</td>
                                        <td style="padding: 5px 0;">%s</td>
                                    </tr>
                                </table>
                                <div style="text-align: center;">
                                    <a href="https://www.deephire.com/login" 
                                       style="display: inline-block; padding: 12px 24px; background-color: #007bff; color: #ffffff; text-decoration: none; font-size: 16px; border-radius: 5px; font-weight: bold;">
                                       Login to Your Account
                                    </a>
                                </div>
                            </td>
                        </tr>
                        <!-- Footer -->
                        <tr>
                            <td style="background-color: #f8f9fa; padding: 20px; text-align: center; border-bottom-left-radius: 8px; border-bottom-right-radius: 8px;">
                                <p style="color: #777777; font-size: 14px; margin: 0;">
                                    &copy; 2025 DeepHire. All rights reserved.
                                </p>
                                <p style="color: #777777; font-size: 14px; margin: 5px 0;">
                                    <a href="https://www.deephire.com/support" style="color: #007bff; text-decoration: none;">Contact Support</a> |
                                    <a href="https://www.deephire.com/privacy" style="color: #007bff; text-decoration: none;">Privacy Policy</a>
                                </p>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
            """;

            // Inject name, username, and password
            String htmlContent = String.format(htmlTemplate, name, name, rawPassword);

            helper.setText(htmlContent, true); // true = HTML
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace(); // Log error
            throw new RuntimeException("Failed to send welcome email", e);
        }
    }

    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("amirouni162@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}
