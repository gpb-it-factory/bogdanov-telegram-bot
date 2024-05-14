package ru.gazprombank.payhub.telegrambot.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface ResponseHandler {
    SendMessage messageHandle(Message message);
}
