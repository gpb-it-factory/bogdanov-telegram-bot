package ru.gazprombank.payhub.telegrambot.util

import org.telegram.telegrambots.meta.api.methods.send.SendMessage

fun createMessage(chatId: String, text: String) =
    SendMessage(chatId, text)
        .apply { enableMarkdown(true) }
        .apply { disableWebPagePreview() }
