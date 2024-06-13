package ru.gazprombank.payhub.telegrambot.client

import org.springframework.web.client.RestTemplate
import org.springframework.http.*
import org.springframework.web.client.HttpStatusCodeException

open class BaseClient(private val restTemplate: RestTemplate) {

    protected fun <T, R> post(path: String, body: T, responseType: Class<R>): ResponseEntity<R> {
        return makeAndSendRequest(HttpMethod.POST, path, body, responseType)
    }

    protected fun <R> get(path: String, responseType: Class<R>): ResponseEntity<R> {
        return makeAndSendRequest(HttpMethod.GET, path, null, responseType)
    }

    private fun <T, R> makeAndSendRequest(
        method: HttpMethod,
        path: String,
        body: T?,
        responseType: Class<R>
    ): ResponseEntity<R> {
        val requestEntity = HttpEntity(body, defaultHeaders())

        val middleServerResponse: ResponseEntity<R> = try {
            restTemplate.exchange(path, method, requestEntity, responseType)
        } catch (e: HttpStatusCodeException) {
            return ResponseEntity.status(e.statusCode).body(e.responseBodyAsByteArray as R)
        }
        return middleServerResponse
    }

    private fun defaultHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.accept = listOf(MediaType.APPLICATION_JSON)
        return headers
    }
}