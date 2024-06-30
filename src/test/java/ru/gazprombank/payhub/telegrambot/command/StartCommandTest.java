package ru.gazprombank.payhub.telegrambot.command;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;
import static ru.gazprombank.payhub.telegrambot.util.TestDataUtils.createChat;

public class StartCommandTest {
    private final StartCommand startCommand = new StartCommand();
    private final AbsSender absSender = Mockito.spy(AbsSender.class);

    @Test
    void testResponseMessage() throws TelegramApiException {
        String expectedResponse = "Добро пожаловать!";
        final User user = new User();
        final Chat chat = createChat(54321L);

        startCommand.execute(absSender, user, chat, new String[]{});

        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(absSender).execute(messageCaptor.capture());
        SendMessage actualMessage = messageCaptor.getValue();
        assertEquals(expectedResponse, actualMessage.getText());
    }
}