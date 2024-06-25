package ru.gazprombank.payhub.telegrambot.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.gazprombank.payhub.telegrambot.command.RegisterCommand;

import static org.mockito.Mockito.mock;

public class RegisterCommandIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private RegisterCommand registerCommand;
    private final AbsSender absSender = mock(AbsSender.class);

    @Test
    void testSuccessfulRegistration() {
        final Long userId = 12345L;
        final String userName = "testUserName";
        final boolean isBot = false;
        final User user = createTelegramUser(userId, userName, isBot);
        final Chat chat = createChat(54321L);
        WireMock.stubFor(
                WireMock.post(WireMock.urlEqualTo("/api/v1/users"))
                        .willReturn(WireMock.aResponse().withStatus(200).withBody("Регистрация успешна!"))
        );

        registerCommand.execute(absSender, user, chat, new String[]{});

        WireMock.verify(
                WireMock.postRequestedFor(WireMock.urlEqualTo("/api/v1/users"))
                        .withRequestBody(WireMock.equalToJson(String.format(
                                """
                                        {"userId":%d,
                                        "userName":"%s"}""
                                        """,
                                userId,
                                userName)))
        );
    }

    private Chat createChat(Long chatId) {
        final Chat chat = new Chat();
        chat.setId(chatId);
        return chat;
    }

    private User createTelegramUser(Long userId, String userName, boolean isBot) {
        final User user = new User();
        user.setId(userId);
        user.setUserName(userName);
        user.setIsBot(isBot);
        return user;
    }
}

