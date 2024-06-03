package com.fiecl.book.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    // something to be added
    @Async // Since we don't want to block the user and make the user wait until the email is sent. IMPORTANT: We also need to add "@EnableAsync" annotation in "BookNetworkApiApplication".
    public void sendEmail(
            String to,
            String username,
            EmailTemplateName emailTemplate,
            String confirmationUrl,
            String activationCode,
            String subject
    ) throws MessagingException {
        String templateName;
        if (emailTemplate == null) {
            templateName = "confirm-email";
        } else {
            templateName = emailTemplate.name();
        }

        // Configuring mail sender
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_MIXED,
                UTF_8.name()
        );
        // Passing some parameters to the email template (which is the html template)
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activation_code", activationCode);

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("contact@fiecl.com");
        helper.setTo(to);
        helper.setSubject(subject);

        // Processing the template
        String template = templateEngine.process(templateName, context);

        helper.setText(template, true);

        mailSender.send(mimeMessage);

    }

}
