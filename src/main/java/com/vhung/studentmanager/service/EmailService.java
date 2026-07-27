package com.vhung.studentmanager.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otpCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Mã OTP đặt lại mật khẩu - Student Manager");
            helper.setText(buildOtpEmailHtml(otpCode), true);   // true = nội dung là HTML

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi email OTP", e);
        }
    }

    private String buildOtpEmailHtml(String otpCode) {
        return """
            <!DOCTYPE html>
            <html>
            <body style="margin:0; padding:0; background-color:#F3F4F6; font-family: Arial, Helvetica, sans-serif;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#F3F4F6; padding: 40px 0;">
                    <tr>
                        <td align="center">
                            <table width="480" cellpadding="0" cellspacing="0" style="background-color:#FFFFFF; border-radius:12px; overflow:hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">

                                <!-- Header -->
                                <tr>
                                    <td style="background-color:#2563EB; padding: 24px; text-align:center;">
                                        <span style="color:#FFFFFF; font-size:20px; font-weight:bold;">
                                            Student Manager
                                        </span>
                                    </td>
                                </tr>

                                <!-- Body -->
                                <tr>
                                    <td style="padding: 32px 40px;">
                                        <h2 style="margin:0 0 12px; color:#111827; font-size:20px;">
                                            Yêu cầu đặt lại mật khẩu
                                        </h2>
                                        <p style="margin:0 0 24px; color:#4B5563; font-size:14px; line-height:1.6;">
                                            Bạn vừa yêu cầu đặt lại mật khẩu cho tài khoản của mình.
                                            Vui lòng sử dụng mã OTP bên dưới để hoàn tất quá trình.
                                        </p>

                                        <!-- OTP Box -->
                                        <table width="100%%" cellpadding="0" cellspacing="0">
                                            <tr>
                                                <td align="center" style="padding: 20px 0;">
                                                    <div style="display:inline-block; background-color:#EFF6FF; border: 1px dashed #2563EB; border-radius:8px; padding: 16px 32px;">
                                                        <span style="font-size:32px; font-weight:bold; letter-spacing:8px; color:#2563EB;">
                                                            %s
                                                        </span>
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>

                                        <p style="margin:24px 0 0; color:#9CA3AF; font-size:13px; text-align:center;">
                                            Mã có hiệu lực trong <strong style="color:#DC2626;">5 phút</strong>.
                                            Vui lòng không chia sẻ mã này với bất kỳ ai.
                                        </p>
                                    </td>
                                </tr>

                                <!-- Footer -->
                                <tr>
                                    <td style="background-color:#F9FAFB; padding: 16px 40px; text-align:center; border-top:1px solid #E5E7EB;">
                                        <span style="font-size:12px; color:#9CA3AF;">
                                            Nếu bạn không yêu cầu điều này, vui lòng bỏ qua email này.
                                        </span>
                                    </td>
                                </tr>

                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(otpCode);
    }
}