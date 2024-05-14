package ru.gazprombank.payhub.telegrambot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class CommandHandlerImpl implements CommandHandler {
    @Override
    public SendMessage commandHandle(Message message) {
        String response;
        switch (message.getText()) {
            case "/ping" -> response = "pong";
            case "/help" -> response = "бог в помощь";
            default -> response = "такой команды нет";
        }
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(response).build();
    }
}
