package ru.gazprombank.payhub.telegrambot.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gazprombank.payhub.telegrambot.command.CurrentBalanceCommand;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static ru.gazprombank.payhub.telegrambot.util.TestDataUtils.createChat;
import static ru.gazprombank.payhub.telegrambot.util.TestDataUtils.createTelegramUser;

public class CurrentBalanceCommandIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private CurrentBalanceCommand command;

    private final AbsSender absSender = spy(AbsSender.class);

    @Test
    @DisplayName("Успешное получение текущего баланса")
    void testSuccessfulFindBalance() throws TelegramApiException {
        final String responseMessage = "Successful";
        final Long userId = 12345L;
        final String userName = "testUserName";
        final boolean isBot = false;
        final User user = createTelegramUser(userId, userName, isBot);
        final Chat chat = createChat(54321L);

        configureStubForFindBalance(userId, responseMessage);

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);

        command.execute(absSender, user, chat, new String[]{});

        assertFindBalanceRequest(userId);
        assertFindBalanceResponse(messageCaptor, responseMessage);
    }

    private void configureStubForFindBalance(final Long userId, final String responseMessage) {
        WireMock.stubFor(
                WireMock.get(WireMock.urlEqualTo(String.format("/api/v1/users/%d/accounts", userId)))
                        .willReturn(WireMock.aResponse().withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\"message\": \"" + responseMessage + "\"}"))
        );
    }

    private void assertFindBalanceRequest(final Long userId) {
        WireMock.verify(
                WireMock.getRequestedFor(WireMock.urlEqualTo(String.format("/api/v1/users/%d/accounts", userId)))
        );
    }

    private void assertFindBalanceResponse(ArgumentCaptor<SendMessage> messageCaptor, final String responseMessage) throws TelegramApiException {
        verify(absSender).execute(messageCaptor.capture());
        SendMessage capturedMessage = messageCaptor.getValue();
        assertEquals(responseMessage, capturedMessage.getText());
    }
}