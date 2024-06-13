package ru.gazprombank.payhub.telegrambot.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Service
import org.springframework.web.util.DefaultUriBuilderFactory
import ru.gazprombank.payhub.telegrambot.dto.CreateUserRequestDto


@Service
class UserClient(@Value("\${middle-server.url}") serverUrl: String, builder: RestTemplateBuilder) : BaseClient(
    builder.uriTemplateHandler(DefaultUriBuilderFactory("$serverUrl/api/v1/users"))
        .requestFactory(HttpComponentsClientHttpRequestFactory()::class.java)
        .build()
) {
    fun create(userRequestDto: CreateUserRequestDto): String {
        return post("", userRequestDto, String::class.java).body ?: ""
    }
}