package com.rishab99058.linkedIn.user_service.utils;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class MailUtil {

    private final JavaMailSender mailSender;

    public void sendWelcomeMail(String recipientEmail, String recipientName) throws MessagingException {
        String subject = "Welcome to LinkedIn!";
        String content =
                "<!DOCTYPE html>" +
                        "<html><head><meta charset='UTF-8'></head>" +
                        "<body style=\"background:#f3f7fa;margin:0;padding:0;font-family:'Segoe UI',Arial,sans-serif;\">" +
                        "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background:#f3f7fa;padding:40px 0;\">" +
                        "<tr><td align=\"center\">" +
                        "<table width=\"600\" style=\"background:#fff;border-radius:10px;box-shadow:0 2px 10px #e0e5ec;overflow:hidden;\">" +
                        "<tr><td style=\"padding:40px 40px 0 40px;\">" +
                        "<h2 style=\"color:#0077b5;font-size:28px;margin:0;\">Hello, " + recipientName + "!</h2>" +
                        "<p style=\"font-size:18px;line-height:1.5;color:#444;margin-top:20px;\">" +
                        "Welcome to <b style='color:#0077b5'>LinkedIn</b>, the professional network that helps you connect, learn, and grow in your career." +
                        "</p>" +
                        "<p style=\"font-size:16px;color:#555;margin-top:10px;\">" +
                        "We're thrilled to have you join our community.<br/>" +
                        "Start building your profile, grow your network, and discover new opportunities today!" +
                        "</p>" +
                        "<div style=\"margin:30px 0;\">" +
                        "<a href='https://www.linkedin.com/' style=\"display:inline-block;padding:12px 28px;background:#0077b5;color:#fff;" +
                        "font-weight:bold;border-radius:5px;text-decoration:none;box-shadow:0 2px 8px #eaeaea;\">Explore LinkedIn</a>" +
                        "</div>" +
                        "</td></tr>" +
                        "<tr><td style=\"background:#f9fafb;padding:18px 40px;text-align:center;font-size:13px;color:#aaa;\">" +
                        "<hr style=\"border:none;border-top:1px solid #ececec;margin:18px 0;\">" +
                        "This is an automated welcome email from LinkedIn.<br/>" +
                        "© LinkedIn Corporation, 2025" +
                        "</td></tr>" +
                        "</table>" +
                        "</td></tr>" +
                        "</table>" +
                        "</body></html>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        helper.setFrom("no-reply@linkedin.com");
        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(content, true); // HTML

        mailSender.send(message);
    }

    public void sendForgotPasswordOtpMail(String recipientEmail, String recipientName, String otp) throws MessagingException {
        String subject = "LinkedIn Password Reset Verification Code";
        String content =
                "<!DOCTYPE html>" +
                        "<html><head><meta charset='UTF-8'></head>" +
                        "<body style=\"background:#f3f7fa;margin:0;padding:0;font-family:'Segoe UI',Arial,sans-serif;\">" +
                        "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background:#f3f7fa;padding:40px 0;\">" +
                        "<tr><td align=\"center\">" +
                        "<table width=\"600\" style=\"background:#fff;border-radius:10px;box-shadow:0 2px 10px #e0e5ec;overflow:hidden;\">" +
                        "<tr><td style=\"padding:40px 40px 0 40px;\">" +
                        "<h2 style=\"color:#0077b5;font-size:28px;margin:0;\">Hi, " + recipientName + "!</h2>" +
                        "<p style=\"font-size:18px;line-height:1.5;color:#444;margin-top:20px;\">" +
                        "We received a request to reset your <b style='color:#0077b5'>LinkedIn</b> password." +
                        "</p>" +
                        "<p style=\"font-size:16px;color:#555;margin-top:10px;\">" +
                        "Enter this verification code to proceed with resetting your password:" +
                        "</p>" +
                        "<div style=\"margin:30px 0;\">" +
                        "<span style=\"display:inline-block;padding:12px 28px;background:#0077b5;color:#fff;" +
                        "font-size:24px;font-weight:bold;letter-spacing:4px;border-radius:5px;\">" + otp + "</span>" +
                        "</div>" +
                        "<p style=\"font-size:16px;color:#888;margin-top:10px;\">" +
                        "This code will expire in 10 minutes for your security." +
                        "</p>" +
                        "</td></tr>" +
                        "<tr><td style=\"background:#f9fafb;padding:18px 40px;text-align:center;font-size:13px;color:#aaa;\">" +
                        "<hr style=\"border:none;border-top:1px solid #ececec;margin:18px 0;\">" +
                        "If you didn’t request a password reset, you can safely ignore this email.<br/>" +
                        "If you need help, please contact LinkedIn support.<br/>" +
                        "© LinkedIn Corporation, 2025" +
                        "</td></tr>" +
                        "</table>" +
                        "</td></tr>" +
                        "</table>" +
                        "</body></html>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        helper.setFrom("no-reply@linkedin.com");
        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }



}
