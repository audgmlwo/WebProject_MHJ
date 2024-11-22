package common;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class MailUtil {

    private static final Properties props = new Properties();

    static {
        try {
            // SMTP 설정 로드
            try (InputStream input = MailUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    throw new IllegalStateException("Unable to find config.properties in resources folder.");
                }
                props.load(input);
            }

            // TrustStore 경로를 상대 경로로 설정
            String trustStorePath = Objects.requireNonNull(
                MailUtil.class.getClassLoader().getResource("smtp_truststore.jks")
            ).getPath();

            System.setProperty("javax.net.ssl.trustStore", trustStorePath);
            System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

            System.out.println("TrustStore Path: " + System.getProperty("javax.net.ssl.trustStore"));
            System.out.println("TrustStore Password: " + System.getProperty("javax.net.ssl.trustStorePassword"));

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("Failed to initialize MailUtil: " + e.getMessage());
        }
    }

    public static void sendEmail(String recipient, String subject, String content) throws MessagingException {
        String host = props.getProperty("smtp.host");
        String port = props.getProperty("smtp.port");
        String username = props.getProperty("smtp.username");
        String password = props.getProperty("smtp.password");

        if (host == null || port == null || username == null || password == null) {
            throw new IllegalStateException("SMTP configuration is not properly initialized.");
        }

        // SMTP 설정
        Properties smtpProps = new Properties();
        smtpProps.put("mail.smtp.host", host);
        smtpProps.put("mail.smtp.port", port);
        smtpProps.put("mail.smtp.auth", "true");
        smtpProps.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(smtpProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // 이메일 메시지 생성
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);
        message.setText(content);

        // 이메일 전송
        Transport.send(message);
        System.out.println("Email sent successfully to " + recipient);
    }
}
