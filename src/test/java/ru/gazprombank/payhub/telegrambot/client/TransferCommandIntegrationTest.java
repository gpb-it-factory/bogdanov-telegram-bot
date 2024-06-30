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
import ru.gazprombank.payhub.telegrambot.command.TransferCommand;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static ru.gazprombank.payhub.telegrambot.util.TestDataUtils.createChat;
import static ru.gazprombank.payhub.telegrambot.util.TestDataUtils.createTelegramUser;

public class TransferCommandIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TransferCommand command;

    private final AbsSender absSender = spy(AbsSender.class);

    @Test
    @DisplayName("Успешное создание перевода")
    void testSuccessfulCreateAccount() throws TelegramApiException {
        final String responseMessage = "Successful";
        final String receiverName = "Иванов";
        final String amount = "100";
        final Long userId = 12345L;
        final String userName = "testUserName";
        final boolean isBot = false;
        final User user = createTelegramUser(userId, userName, isBot);
        final Chat chat = createChat(54321L);

        configureStubForTransfer(responseMessage, user, receiverName, amount);

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);

        command.execute(absSender, user, chat, new String[]{receiverName, amount});

        assertTransferRequest(user, receiverName, amount);
        assertTransferResponse(messageCaptor, responseMessage);
    }

    private void configureStubForTransfer(final String responseMessage, final User user, final String receiverName, final String amount) {
        WireMock.stubFor(
                WireMock.post(WireMock.urlEqualTo("/api/v1/transfers"))
                        .willReturn(WireMock.aResponse().withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\"message\": \"" + responseMessage + "\"}"))
        );
    }

    private void assertTransferRequest(final User user, final String receiverName, final String amount) {
        WireMock.verify(
                WireMock.postRequestedFor(WireMock.urlEqualTo("/api/v1/transfers"))
                        .withRequestBody(
                                WireMock.equalToJson("{\n  \"from\": \"" + user.getUserName() + "\",\n  \"to\": \"" + receiverName + "\",\n  \"amount\": \"" + amount + "\"\n}\n"))
        );
    }

    private void assertTransferResponse(ArgumentCaptor<SendMessage> messageCaptor, final String responseMessage) throws TelegramApiException {
        verify(absSender).execute(messageCaptor.capture());
        SendMessage capturedMessage = messageCaptor.getValue();
        assertEquals(responseMessage, capturedMessage.getText());
    }
}