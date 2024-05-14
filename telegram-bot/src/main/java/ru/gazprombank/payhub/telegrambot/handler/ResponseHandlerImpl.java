package ru.gazprombank.payhub.telegrambot.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class ResponseHandlerImpl implements ResponseHandler {
    private final CommandHandler commandHandler;

    @Override
    public SendMessage messageHandle(Message message) {
        if (message.isCommand()) {
            return commandHandler.commandHandle(message);
        }
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("этот сценарий не обрабатывается")
                .build();
    }
}
