package ru.gazprombank.payhub.telegrambot.client;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

@SpringBootTest
@AutoConfigureWireMock(port = 7070)
public abstract class AbstractIntegrationTest {
}
