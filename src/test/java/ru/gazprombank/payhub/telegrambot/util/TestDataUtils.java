package ru.gazprombank.payhub.telegrambot.util;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

public final class TestDataUtils {
    private TestDataUtils() {
    }

    public static Chat createChat(Long chatId) {
        final Chat chat = new Chat();
        chat.setId(chatId);
        return chat;
    }

    public static User createTelegramUser(Long userId, String userName, boolean isBot) {
        final User user = new User();
        user.setId(userId);
        user.setUserName(userName);
        user.setIsBot(isBot);
        return user;
    }
}
