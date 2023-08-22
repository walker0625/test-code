package com.jmw.testcode.spring.client.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailSendClient {

    public boolean sendEmail(String fromEmail, String toEmail, String subject, String content) {

        log.info("메일 전송");

        throw new IllegalArgumentException("메일 전송"); // 메일 전송으로 간주
    }

}
