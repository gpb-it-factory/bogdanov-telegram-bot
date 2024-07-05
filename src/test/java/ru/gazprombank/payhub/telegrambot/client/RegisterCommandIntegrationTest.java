package ru.gazprombank.payhub.telegrambot.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.gazprombank.payhub.telegrambot.command.RegisterCommand;

import static org.mockito.Mockito.mock;
import static ru.gazprombank.payhub.telegrambot.util.TestDataUtils.createChat;
import static ru.gazprombank.payhub.telegrambot.util.TestDataUtils.createTelegramUser;

public class RegisterCommandIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private RegisterCommand registerCommand;

    private final AbsSender absSender = mock(AbsSender.class);

    @Test
    @DisplayName("Успешная регистрация пользователя")
    void testSuccessfulRegistration() {
        final String responseMessage = "Successful";
        final Long userId = 12345L;
        final String userName = "testUserName";
        final boolean isBot = false;
        final User user = createTelegramUser(userId, userName, isBot);
        final Chat chat = createChat(54321L);

        configureStubForRegistration(responseMessage, userId, userName);

        registerCommand.execute(absSender, user, chat, new String[]{});

        assertRegistrationRequest(userId, userName);
    }

    private void configureStubForRegistration(final String responseMessage, final Long userId, final String userName) {
        WireMock.stubFor(
                WireMock.post(WireMock.urlEqualTo("/api/v1/users"))
                        .willReturn(WireMock.aResponse().withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\"message\": \"" + responseMessage + "\"}"))
        );
    }

    private void assertRegistrationRequest(final Long userId, final String userName) {
        WireMock.verify(
                WireMock.postRequestedFor(WireMock.urlEqualTo("/api/v1/users"))
                        .withRequestBody(WireMock.equalToJson(String.format(
                                "{\"userId\":%d,\n" +
                                "\"userName\":\"%s\"}\n",
                                userId,
                                userName)))
        );
    }
}