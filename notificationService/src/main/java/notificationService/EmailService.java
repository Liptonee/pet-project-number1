package notificationService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final ObjectMapper objectMapper;

    /**
     * Отправляет email получателю с указанной темой и текстом.
     * Если текст является JSON, он будет передан как есть.
     */
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            mailSender.send(message);
            log.info("Email sent to {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }

    /**
     * Сериализует объект в JSON и отправляет email.
     */
    public void sendEventAsJson(String to, String subject, Object event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            sendEmail(to, subject, json);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event to JSON", e);
        }
    }
}