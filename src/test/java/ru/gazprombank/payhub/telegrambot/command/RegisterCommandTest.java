package ru.gazprombank.payhub.telegrambot.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.gazprombank.payhub.telegrambot.client.UserClient;
import ru.gazprombank.payhub.telegrambot.dto.CreateUserRequestDto;
import ru.gazprombank.payhub.telegrambot.dto.ResponseMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.gazprombank.payhub.telegrambot.util.TestDataUtils.createChat;
import static ru.gazprombank.payhub.telegrambot.util.TestDataUtils.createTelegramUser;

class RegisterCommandTest {

    private final UserClient userClient = mock(UserClient.class);
    private final RegisterCommand command = new RegisterCommand(userClient);
    private final AbsSender absSender = spy(AbsSender.class);

    @Test
    @DisplayName("Проверяем передачу UserDto в клиент")
    void testSendUserDto() {
        final Long userId = 12345L;
        final String userName = "testUserName";
        final boolean isBot = false;
        final User user = createTelegramUser(userId, userName, isBot);
        final Chat chat = createChat(54321L);
        final String expectedResponse = "Регистрация успешна!";

        configureStubForUserClient(expectedResponse);

        executeCommand(user, chat);

        assertUserDtoSentToClient(user);
    }

    @Test
    @DisplayName("Проверяем возвращаемое сообщение")
    void testResponseMessage() throws Exception {
        final Long userId = 12345L;
        final String userName = "testUserName";
        final boolean isBot = false;
        final User user = createTelegramUser(userId, userName, isBot);
        final Chat chat = createChat(54321L);
        final String expectedResponse = "Регистрация успешна!";
        final ResponseMessage response = new ResponseMessage(expectedResponse);

        configureStubForUserClient(expectedResponse);

        executeCommand(user, chat);

        assertResponseMessage(chat, expectedResponse);
    }

    @Test
    @DisplayName("Регистрация бота")
    void testBotRegistration() throws Exception {
        final String expectedResponse = "Вы не можете зарегистрировать бота";
        final Long userId = 12345L;
        final String userName = "testUserName";
        final boolean isBot = true;
        final User user = createTelegramUser(userId, userName, isBot);
        final Chat chat = createChat(54321L);

        executeCommand(user, chat);

        assertResponseMessage(chat, expectedResponse);
        verifyNoUserCreation();
    }

    private void configureStubForUserClient(final String expectedResponse) {
        when(userClient.create(any())).thenReturn(new ResponseMessage(expectedResponse));
    }

    private void executeCommand(final User user, final Chat chat) {
        command.execute(absSender, user, chat, new String[]{});
    }

    private void assertUserDtoSentToClient(final User user) {
        ArgumentCaptor<CreateUserRequestDto> requestCaptor = ArgumentCaptor.forClass(CreateUserRequestDto.class);
        verify(userClient).create(requestCaptor.capture());
        CreateUserRequestDto actualRequest = requestCaptor.getValue();
        assertEquals(user.getId(), actualRequest.getUserId());
        assertEquals(user.getUserName(), actualRequest.getUserName());
    }

    private void assertResponseMessage(final Chat chat, final String expectedResponse) throws Exception {
        ArgumentCaptor<SendMessage> messageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(absSender).execute(messageCaptor.capture());
        SendMessage actualMessage = messageCaptor.getValue();
        assertEquals(chat.getId().toString(), actualMessage.getChatId());
        assertEquals(expectedResponse, actualMessage.getText());
    }

    private void verifyNoUserCreation() {
        verify(userClient, never()).create(any(CreateUserRequestDto.class));
    }
}