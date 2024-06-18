package ru.gazprombank.payhub.telegrambot.dto

data class CreateTransferRequestDto(val from: Long, val to: String, val amount: String)
