package com.jmw.testcode.spring.api.service.mail;

import com.jmw.testcode.spring.client.mail.MailSendClient;
import com.jmw.testcode.spring.domain.history.mail.MailSendHistory;
import com.jmw.testcode.spring.domain.history.mail.MailSendHistoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    //@Spy > Mockito.doReturn()

    @Mock
    private MailSendClient mailSendClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks
    private MailService mailService;

    @DisplayName("메일 전송 테스트")
    @Test
    void sendMail() {
        // given

        // 통합테스트가 아닌 경우는 bean을 사용하지 않으므로 @MockBean 사용이 어려움
        // @Mock
        // MailSendClient mailSendClient = Mockito.mock(MailSendClient.class);
        // MailSendHistoryRepository mailSendHistoryRepository = Mockito.mock(MailSendHistoryRepository.class);

        // @InjectMocks
        // MailService mailService = new MailService(mailSendClient, mailSendHistoryRepository);

        //Mockito.when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
        //       .thenReturn(true);
        BDDMockito.given(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
                  .willReturn(true);


        // when
        boolean result = mailService.sendMail("", "", "", "");


        // then
        Assertions.assertThat(result).isTrue();
        Mockito.verify(mailSendHistoryRepository, Mockito.times(1)).save(Mockito.any(MailSendHistory.class));
    }

}