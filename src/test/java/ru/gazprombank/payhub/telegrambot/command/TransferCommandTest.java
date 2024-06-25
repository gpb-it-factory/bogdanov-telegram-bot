package ru.gazprombank.payhub.telegrambot.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gazprombank.payhub.telegrambot.client.TransferClient;
import ru.gazprombank.payhub.telegrambot.dto.CreateTransferRequestDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.gazprombank.payhub.telegrambot.util.TestDataUtils.createChat;
import static ru.gazprombank.payhub.telegrambot.util.TestDataUtils.createTelegramUser;

public class TransferCommandTest {
    private final TransferClient transferClient = mock(TransferClient.class);
    private final TransferCommand command = new TransferCommand(transferClient);
    private final AbsSender absSender = spy(AbsSender.class);

    @Test
    void testBotCreateAccount() throws TelegramApiException {
        String expectedMessage = "Боты не поддерживаются";
        final Long userId = 12345L;
        final String userName = "testUserName";
        final boolean isBot = true;
        final User user = createTelegramUser(userId, userName, isBot);
        final Chat chat = createChat(54321L);

        command.execute(absSender, user, chat, new String[]{"name", "100.00"});

        ArgumentCaptor<SendMessage> requestCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(absSender).execute(requestCaptor.capture());
        SendMessage actualMessage = requestCaptor.getValue();
        assertEquals(chat.getId().toString(), actualMessage.getChatId());
        assertEquals(expectedMessage, actualMessage.getText());

        verify(transferClient, never()).create(any(CreateTransferRequestDto.class));
    }

    @Test
    @DisplayName("Не передаю имя получателя")
    void testCreateAccountWithoutName() throws TelegramApiException {
        String expectedMessage = "Команда должна быть вида " +
                "```Help" +
                " /transfer [Имя получателя в телеграмме] [Сумма перевода]\n" +
                "   Пример:\n" +
                " /transfer Popov 100```";
        final Long userId = 12345L;
        final String userName = "testUserName";
        final boolean isBot = false;
        final User user = createTelegramUser(userId, userName, isBot);
        final Chat chat = createChat(54321L);

        command.execute(absSender, user, chat, new String[]{"100.00"});

        ArgumentCaptor<SendMessage> requestCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(absSender).execute(requestCaptor.capture());
        SendMessage actualMessage = requestCaptor.getValue();
        assertEquals(chat.getId().toString(), actualMessage.getChatId());
        assertEquals(expectedMessage, actualMessage.getText());

        verify(transferClient, never()).create(any(CreateTransferRequestDto.class));
    }

    @Test
    @DisplayName("Не передаю сумму перевода")
    void testCreateAccountWithoutBill() throws TelegramApiException {
        String expectedMessage = "Команда должна быть вида " +
                "```Help" +
                " /transfer [Имя получателя в телеграмме] [Сумма перевода]\n" +
                "   Пример:\n" +
                " /transfer Popov 100```";
        final Long userId = 12345L;
        final String userName = "testUserName";
        final boolean isBot = false;
        final User user = createTelegramUser(userId, userName, isBot);
        final Chat chat = createChat(54321L);

        command.execute(absSender, user, chat, new String[]{"name"});

        ArgumentCaptor<SendMessage> requestCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(absSender).execute(requestCaptor.capture());
        SendMessage actualMessage = requestCaptor.getValue();
        assertEquals(chat.getId().toString(), actualMessage.getChatId());
        assertEquals(expectedMessage, actualMessage.getText());

        verify(transferClient, never()).create(any(CreateTransferRequestDto.class));
    }

    @Test
    @DisplayName("Не передаю сумму перевода и имя")
    void testCreateAccountWithoutParam() throws TelegramApiException {
        String expectedMessage = "Команда должна быть вида " +
                "```Help" +
                " /transfer [Имя получателя в телеграмме] [Сумма перевода]\n" +
                "   Пример:\n" +
                " /transfer Popov 100```";
        final Long userId = 12345L;
        final String userName = "testUserName";
        final boolean isBot = false;
        final User user = createTelegramUser(userId, userName, isBot);
        final Chat chat = createChat(54321L);

        command.execute(absSender, user, chat, new String[]{});

        ArgumentCaptor<SendMessage> requestCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(absSender).execute(requestCaptor.capture());
        SendMessage actualMessage = requestCaptor.getValue();
        assertEquals(chat.getId().toString(), actualMessage.getChatId());
        assertEquals(expectedMessage, actualMessage.getText());

        verify(transferClient, never()).create(any(CreateTransferRequestDto.class));
    }

    @Test
    @DisplayName("Передаю 3 параметра")
    void testCreateAccountForTwoPeople() throws TelegramApiException {
        String expectedMessage = "Команда должна быть вида " +
                "```Help" +
                " /transfer [Имя получателя в телеграмме] [Сумма перевода]\n" +
                "   Пример:\n" +
                " /transfer Popov 100```";
        final Long userId = 12345L;
        final String userName = "testUserName";
        final boolean isBot = false;
        final User user = createTelegramUser(userId, userName, isBot);
        final Chat chat = createChat(54321L);

        command.execute(absSender, user, chat, new String[]{"name", "name", "100"});

        ArgumentCaptor<SendMessage> requestCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(absSender).execute(requestCaptor.capture());
        SendMessage actualMessage = requestCaptor.getValue();
        assertEquals(chat.getId().toString(), actualMessage.getChatId());
        assertEquals(expectedMessage, actualMessage.getText());

        verify(transferClient, never()).create(any(CreateTransferRequestDto.class));
    }
}
