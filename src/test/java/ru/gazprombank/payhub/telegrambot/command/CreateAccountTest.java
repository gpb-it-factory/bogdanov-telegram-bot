package ru.gazprombank.payhub.telegrambot.command;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gazprombank.payhub.telegrambot.client.AccountClient;
import ru.gazprombank.payhub.telegrambot.dto.CreateAccountRequestDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static ru.gazprombank.payhub.telegrambot.util.TestDataUtils.createChat;
import static ru.gazprombank.payhub.telegrambot.util.TestDataUtils.createTelegramUser;

public class CreateAccountTest {
    private final AccountClient accountClient = mock(AccountClient.class);
    private final CreateAccountCommand command = new CreateAccountCommand(accountClient);
    private final AbsSender absSender = spy(AbsSender.class);

    @Test
    void testBotCreateAccount() throws TelegramApiException {
        String expectedMessage = "Вы не можете зарегистрировать бота";
        final Long userId = 12345L;
        final String userName = "testUserName";
        final boolean isBot = true;
        final User user = createTelegramUser(userId, userName, isBot);
        final Chat chat = createChat(54321L);

        command.execute(absSender, user, chat, new String[]{});

        ArgumentCaptor<SendMessage> requestCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(absSender).execute(requestCaptor.capture());
        SendMessage actualMessage = requestCaptor.getValue();
        assertEquals(chat.getId().toString(), actualMessage.getChatId());
        assertEquals(expectedMessage, actualMessage.getText());

        verify(accountClient, never()).create(anyLong(), any(CreateAccountRequestDto.class));
    }
}
