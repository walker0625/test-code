package com.jmw.testcode.spring.api.service.mail;

import com.jmw.testcode.spring.client.mail.MailSendClient;
import com.jmw.testcode.spring.domain.history.mail.MailSendHistory;
import com.jmw.testcode.spring.domain.history.mail.MailSendHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailService {

    private final MailSendClient mailSendClient;
    private final MailSendHistoryRepository mailSendHistoryRepository;

    public boolean sendMail(String fromEmail, String toEmail, String subject, String content) {

        boolean result = mailSendClient.sendEmail(fromEmail, toEmail, subject, content);

        if(result) {
            mailSendHistoryRepository.save(MailSendHistory.builder()
                    .fromEmail(fromEmail)
                    .toEmail(toEmail)
                    .subject(subject)
                    .content(content)
                    .build()
            );
            return true;
        }

        return false;
    }

}
