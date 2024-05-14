package ru.gazprombank.payhub.telegrambot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.gazprombank.payhub.telegrambot.handler.ResponseHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final ResponseHandler responseHandler;
    @Value(value = "${telegram.bot.name}")
    private String botName;
    @Value(value = "${telegram.bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Update с сообщением {}", update.getMessage().getText());
        try {
            execute(responseHandler.messageHandle(update.getMessage()));
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки {}", e.getMessage());
        }
    }
}
