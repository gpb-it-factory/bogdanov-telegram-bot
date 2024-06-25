package ru.gazprombank.payhub.telegrambot.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.junit.jupiter.api.Assertions.assertAll
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class MessageUtilKtTest {
    @Test
    fun `testCreateMessage should create SendMessage with correct parameters`() {
        val chatId = "12345"
        val text = "Test message"

        val message = createMessage(chatId, text)

        assertSendMessageParameters(message, chatId, text)
    }

    @Test
    fun `testCreateMessage should create SendMessage with correct parameters with Emoji`() {
        val chatId = "12345"
        val text = "Test message with emoji 👍"

        val message = createMessage(chatId, text)

        assertSendMessageParameters(message, chatId, text)
    }

    private fun assertSendMessageParameters(message: SendMessage, chatId: String, text: String) {
        assertAll({
            assertNotNull(message)
            assertEquals(chatId, message.chatId)
            assertEquals(text, message.text)
            assertEquals(ParseMode.MARKDOWN, message.parseMode)
            assertTrue(message.disableWebPagePreview)
        })
    }
}

