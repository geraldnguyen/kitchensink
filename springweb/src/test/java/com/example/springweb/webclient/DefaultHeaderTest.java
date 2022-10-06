package com.example.springweb.webclient;

import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultHeaderTest {
    public static final String X_MOCK_HEADER = "X-MOCK-HEADER";
    private static MockWebServer mockBackEnd;
    private static String baseUrl;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();

        baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void setFixedHeader() throws InterruptedException {
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-MOCK-HEADER", "MOCK VALUES")
                .build()
                ;
        webClient.get()
                .uri("/employee/100")
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();


        RecordedRequest recordedRequest = mockBackEnd.takeRequest();

        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/employee/100", recordedRequest.getPath());
        assertEquals("MOCK VALUES", recordedRequest.getHeader(X_MOCK_HEADER));
    }


    @Test
    void setChangingHeader() throws InterruptedException {
        AtomicInteger incrementer = new AtomicInteger(0);
        Function<ClientRequest, Mono<ClientRequest>> headerSetter = clientRequest -> {
            String headerValue = String.valueOf(incrementer.incrementAndGet());
            var newClientRequest = ClientRequest.from(clientRequest)
                    .header(X_MOCK_HEADER, headerValue)
                    .build();
            return Mono.just(newClientRequest);
        };

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(ExchangeFilterFunction.ofRequestProcessor(headerSetter))
                .build()
                ;

        // take 1
        webClient.get()
                .uri("/employee/100")
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertEquals("1", recordedRequest.getHeader("X-MOCK-HEADER"));

        // take 2
        webClient.get()
                .uri("/employee/100")
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();
        recordedRequest = mockBackEnd.takeRequest();
        assertEquals("2", recordedRequest.getHeader("X-MOCK-HEADER"));
    }

}
