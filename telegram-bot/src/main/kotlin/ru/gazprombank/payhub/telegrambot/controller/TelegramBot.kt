package ru.gazprombank.payhub.telegrambot.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Update

import ru.gazprombank.payhub.telegrambot.util.createMessage

@Component
class TelegramBot(
    commands: Set<BotCommand>,
    @Value("\${telegram.bot.token}")
    token: String,
) : TelegramLongPollingCommandBot(token) {

    @Value("\${telegram.bot.name}")
    private val botName: String = ""

    init {
        registerAll(*commands.toTypedArray())
    }

    override fun getBotUsername(): String = botName

    override fun processNonCommandUpdate(update: Update) {
        if (update.hasMessage()) {
            val chatId = update.message.chatId.toString()
            if (update.message.hasText()) {
                execute(createMessage(chatId, "Текст пока не обрабатывается"))
            } else {
                execute(createMessage(chatId, "Я понимаю только команды!"))
            }
        }
    }
}