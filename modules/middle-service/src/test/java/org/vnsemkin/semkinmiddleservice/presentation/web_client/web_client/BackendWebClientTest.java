package org.vnsemkin.semkinmiddleservice.presentation.web_client.web_client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.vnsemkin.semkinmiddleservice.SemkinMiddleServiceApplicationTests;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendErrorResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendRegistrationReq;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendRespUuid;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;
import org.vnsemkin.semkinmiddleservice.domain.models.ErrorModel;
import org.vnsemkin.semkinmiddleservice.presentation.web_client.BackendClientInterfaceImp;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BackendWebClientTest extends SemkinMiddleServiceApplicationTests {
    private final static String VALID_UUID = "5f59e024-03c7-498d-9fc9-b8b15fd49c47";
    private final static String USER_CREATED = "User created.";
    private final static String TEST = "Test";
    private final static String DELIMITER = "/";
    private final static String USERS_ENDPOINT = "/v2/users";
    private final static long TG_USER_ID = 137264783L;
    private final static String TG_USERNAME = "Test";

    BackendRegistrationReq req = new BackendRegistrationReq(TG_USER_ID, TG_USERNAME);
    @Autowired
    BackendClientInterfaceImp backendWebClient;
    @Autowired
    ObjectMapper objectMapper;

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setup() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
    }

    @AfterAll
    public static void teardown() {
        wireMockServer.stop();
    }

    @Test
    public void whenRegisterCustomerOnBackend_Success() {
        stubFor(post(USERS_ENDPOINT)
            .willReturn(aResponse()
                .withStatus(HttpStatus.NO_CONTENT.value())));

        Result<String, BackendErrorResponse> resultBackendDto = backendWebClient
            .registerCustomer(req);

        verify(postRequestedFor(urlEqualTo(USERS_ENDPOINT)));
        assertNotNull(resultBackendDto);
        assertTrue(resultBackendDto.isSuccess());
        assertTrue(resultBackendDto.getData().isPresent());
        assertEquals(resultBackendDto.getData().get(), USER_CREATED);
    }

    @Test
    public void whenRegisterCustomerOnBackend_Error() throws JsonProcessingException {
        ErrorModel errorModel = new ErrorModel(TEST, TEST, TEST, TEST);
        String modelErrorAsJson = objectMapper.writeValueAsString(errorModel);
        stubFor(post(urlEqualTo(USERS_ENDPOINT))
            .willReturn(aResponse()
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(modelErrorAsJson)));

        Result<String, BackendErrorResponse> resultBackendDto = backendWebClient.registerCustomer(req);

        verify(postRequestedFor(urlEqualTo(USERS_ENDPOINT)));
        assertNotNull(resultBackendDto);
        assertTrue(resultBackendDto.isError());
        assertFalse(resultBackendDto.isSuccess());
        assertTrue(resultBackendDto.getError().isPresent());
        assertEquals(resultBackendDto.getError().get().message(), TEST);
        assertEquals(resultBackendDto.getError().get().code(), TEST);
        assertEquals(resultBackendDto.getError().get().type(), TEST);
        assertEquals(resultBackendDto.getError().get().traceId(), TEST);
    }

    @Test
    public void whenRequestCustomerUuidWithValidUser_Success() throws JsonProcessingException {
        BackendRespUuid backendRespUuid = new BackendRespUuid(VALID_UUID);
        String modelErrorAsJson = objectMapper.writeValueAsString(backendRespUuid);
        stubFor(get(USERS_ENDPOINT + DELIMITER + TG_USER_ID)
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(modelErrorAsJson)));

        Result<BackendRespUuid, BackendErrorResponse> customerUuid =
            backendWebClient.getCustomerUuid(req.userId());

        verify(getRequestedFor(urlEqualTo(USERS_ENDPOINT + DELIMITER + TG_USER_ID)));
        assertNotNull(customerUuid);
        assertEquals(VALID_UUID, customerUuid.getData().get().uuid());
    }

    @Test
    public void whenRequestCustomerUuidWithInvalidUser_Error() throws JsonProcessingException {
        ErrorModel errorModel = new ErrorModel(TEST, TEST, TEST, TEST);
        String modelErrorAsJson = objectMapper.writeValueAsString(errorModel);
        stubFor(get(USERS_ENDPOINT + DELIMITER + TG_USER_ID)
            .willReturn(aResponse()
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(modelErrorAsJson)));

        Result<BackendRespUuid, BackendErrorResponse> customerUuid =
            backendWebClient.getCustomerUuid(req.userId());

        verify(getRequestedFor(urlEqualTo(USERS_ENDPOINT + DELIMITER + TG_USER_ID)));
        assertNotNull(customerUuid);
        assertEquals(TEST, customerUuid.getError().get().message());
    }
}
