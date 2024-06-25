package ru.gazprombank.payhub.telegrambot.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gazprombank.payhub.telegrambot.command.CreateAccountCommand;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static ru.gazprombank.payhub.telegrambot.util.TestDataUtils.createChat;
import static ru.gazprombank.payhub.telegrambot.util.TestDataUtils.createTelegramUser;

public class CreateAccountCommandIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private CreateAccountCommand command;
    private final AbsSender absSender = spy(AbsSender.class);

    @Test
    void testSuccessfulCreateAccount() throws TelegramApiException {
        final String responseMessage = "Successful";
        final String accountName = "Акционный";
        final Long userId = 12345L;
        final String userName = "testUserName";
        final boolean isBot = false;
        final User user = createTelegramUser(userId, userName, isBot);
        final Chat chat = createChat(54321L);
        WireMock.stubFor(
                WireMock.post(WireMock.urlEqualTo(String.format("/api/v1/users/%d/accounts", userId)))
                        .willReturn(WireMock.aResponse().withStatus(200).withBody(responseMessage))
        );

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);

        command.execute(absSender, user, chat, new String[]{accountName});

        WireMock.verify(
                WireMock.postRequestedFor(WireMock.urlEqualTo(String.format("/api/v1/users/%d/accounts", userId)))
                        .withRequestBody(WireMock.equalToJson(String.format(
                                """
                                        {"accountName":"%s"}
                                        """,
                                accountName)))
        );

        verify(absSender).execute(messageCaptor.capture());
        SendMessage capturedMessage = messageCaptor.getValue();
        assertEquals(responseMessage, capturedMessage.getText());
    }
}
