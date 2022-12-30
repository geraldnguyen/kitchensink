package com.example.springweb.webclient;


import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.RetrySpec;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RetriableRequestTest {
    public static final String PATH = "/employee/100";
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

    @ParameterizedTest
    @EnumSource(value = HttpStatus.class, mode = EnumSource.Mode.INCLUDE, names = {
            "BAD_REQUEST", "UNAUTHORIZED", "FORBIDDEN",
            "SERVICE_UNAVAILABLE", "INTERNAL_SERVER_ERROR"
    })
    void throwExceptionWhenReceiveNon200Response(HttpStatus status) throws InterruptedException {
        // request
        var webClient = WebClient.builder().baseUrl(baseUrl).build();
        var request = webClient.get()
                .uri(PATH)
                .retrieve()
                .bodyToMono(String.class);

        // response to first request with fail status
        mockBackEnd.enqueue(new MockResponse().setResponseCode(status.value()));

        // assert that exception thrown due to non-200 response
        var ex = assertThrows(WebClientResponseException.class, () -> request.block());
        assertEquals(status, ex.getStatusCode());

        // extra assert that the path is correct
        assertEquals(PATH, mockBackEnd.takeRequest().getPath());
    }

    @Test
    void retryUnChanged() throws InterruptedException {
        // request
        var webClient = WebClient.builder().baseUrl(baseUrl).build();
        var request = webClient.get()
                .uri(PATH)
                .retrieve()
                .bodyToMono(String.class)
                ;
        var requestWithRetry = request.retryWhen(RetrySpec.max(1)        // retry at most once
                .filter(e -> e instanceof WebClientResponseException.ServiceUnavailable));

        // response to first request with a SERVICE_UNAVAILABLE
        mockBackEnd.enqueue(new MockResponse().setResponseCode(HttpStatus.SERVICE_UNAVAILABLE.value()));
        // response to 2nd request with 200
        mockBackEnd.enqueue(new MockResponse().setBody("Successful!"));

        // no more WebClientResponseException.ServiceUnavailable exception
        assertDoesNotThrow(() -> requestWithRetry.block());

        // assert that there are 2 requests sent
        assertEquals(PATH, mockBackEnd.takeRequest().getPath());
        assertEquals(PATH, mockBackEnd.takeRequest().getPath());
    }

    interface TokenSupplier {
        String getToken();
        void refreshToken();
    }

    @Test
    void retryIf401WithDifferentToken() throws InterruptedException {
        String firstToken = "FIRST TOKEN";
        String secondToken = "SECOND TOKEN";
        TokenSupplier tokenSupplier = spy(new TokenSupplier() {
            String currentToken = firstToken;

            @Override
            public String getToken() {
                return currentToken;
            }

            @Override
            public void refreshToken() {
                currentToken = secondToken;
            }
        });

        // request
        var webClient = WebClient.builder().baseUrl(baseUrl).build();
        /*
        The below doesn't work because webclient probably reuses the request instance
        var request = webClient.get()
                .uri("/employee/100")
                .headers(headers -> headers.set(HttpHeaders.AUTHORIZATION, tokenSupplier.getToken()))
                .retrieve()
                .bodyToMono(String.class)
                ;
        */
        var request = Mono.defer(() -> webClient.get()
                .uri(PATH)
                .headers(headers -> headers.set(HttpHeaders.AUTHORIZATION, tokenSupplier.getToken()))
                .retrieve()
                .bodyToMono(String.class)
        );

        var requestWithRetry = request.retryWhen(RetrySpec.max(1)        // retry at most once
                .doBeforeRetry(retrySignal -> tokenSupplier.refreshToken())
                .filter(e -> e instanceof WebClientResponseException.Unauthorized));


        // response to first request with a 401
        mockBackEnd.enqueue(new MockResponse().setResponseCode(HttpStatus.UNAUTHORIZED.value()));
        // response to 2nd request with 200
        mockBackEnd.enqueue(new MockResponse().setBody("Successful!"));

        // no more WebClientResponseException.Unauthorized exception
        assertDoesNotThrow(() -> requestWithRetry.block());

        // assert that first request failed with a 401 with firstToken sent
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertEquals(firstToken, recordedRequest.getHeader(HttpHeaders.AUTHORIZATION));

        // assert that 2nd request passed with secondToken sent
        recordedRequest = mockBackEnd.takeRequest();
        assertEquals(secondToken, recordedRequest.getHeader(HttpHeaders.AUTHORIZATION));

        // verify that tokenSupplier is invoked
        verify(tokenSupplier, times(2)).getToken(); //  twice
        verify(tokenSupplier).refreshToken();
    }

}
