package org.vnsemkin.semkinmiddleservice.application.usecases;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.AccountInfoResponse;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.AccountRegistrationReq;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendErrorResponse;
import org.vnsemkin.semkinmiddleservice.application.external.BackendClientInterface;
import org.vnsemkin.semkinmiddleservice.application.repositories.CustomerRepository;
import org.vnsemkin.semkinmiddleservice.domain.models.Account;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.AccountEntity;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.CustomerEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountRegistrationServiceTest {
    static final long ID = 123L;
    static long TG_ID = 32382763L;
    static final String FIRSTNAME = "Ivan";
    static final String STRING_STUB = "Stub";
    static final String USERNAME = "Ivanov";
    static final String EMAIL = "ivanov@test.ru";
    static final String PASSWORD_HASH = "dsgdhskdj";
    static final String UUID = "123e4567-e89b-12d3-a456-426614174000";
    static final BigDecimal REGISTRATION_ACCOUNT_BONUS = new BigDecimal("5000.00");
    static final String ACCOUNT_ALREADY_EXIST = "У Вас уже открыть счет.";
    static final String USER_NOT_EXIST = "Пользователь не зарегистрирован.";
    static final String DEFAULT_ACCOUNT_NAME = "Акционный";

    @Mock
    CustomerRepository customerRepository;
    @Mock
    BackendClientInterface backendClientInterface;

    @InjectMocks
    AccountRegistrationService accountRegistrationService;

    @Test
    void shouldReturnAccountAlreadyExist() {
        CustomerEntity customerEntity = getCustomerEntity();
        customerEntity.setAccount(new AccountEntity());

        when(customerRepository.findByTgId(TG_ID)).thenReturn(Optional.of(customerEntity));

        Result<Account, String> result = accountRegistrationService.register(TG_ID);

        assertTrue(result.isError());
        assertEquals(ACCOUNT_ALREADY_EXIST, result.getError().orElse(null));
    }

    @Test
    void shouldReturnUserNotExist() {
        when(customerRepository.findByTgId(TG_ID)).thenReturn(Optional.empty());

        Result<Account, String> result = accountRegistrationService.register(TG_ID);

        assertTrue(result.isError());
        assertEquals(USER_NOT_EXIST, result.getError().orElse(null));
    }

    @Test
    void shouldRegisterAccountSuccessfully() {
        CustomerEntity customerEntity = getCustomerEntity();
        when(customerRepository.findByTgId(TG_ID)).thenReturn(Optional.of(customerEntity));
        when(backendClientInterface.registerAccount(new AccountRegistrationReq(TG_ID, DEFAULT_ACCOUNT_NAME)))
            .thenReturn(Result.success("Success"));
        when(backendClientInterface.getAccount(customerEntity.getTgId()))
            .thenReturn(Result.success(new AccountInfoResponse(UUID, DEFAULT_ACCOUNT_NAME, REGISTRATION_ACCOUNT_BONUS)));

        Result<Account, String> register = accountRegistrationService.register(TG_ID);

        assertTrue(register.isSuccess());
        assertNotNull(register.getData().orElse(null));
    }

    @Test
    void shouldHandleBackendRegistrationFailure() {
        CustomerEntity customerEntity = getCustomerEntity();
        when(customerRepository.findByTgId(TG_ID)).thenReturn(Optional.of(customerEntity));
        when(backendClientInterface.registerAccount(new AccountRegistrationReq(TG_ID, DEFAULT_ACCOUNT_NAME)))
            .thenReturn(Result.error(new BackendErrorResponse(STRING_STUB, STRING_STUB, STRING_STUB, STRING_STUB)));

        Result<Account, String> result = accountRegistrationService.register(TG_ID);

        assertTrue(result.isError());
        assertNotNull(result.getError().orElse(null));
    }

    CustomerEntity getCustomerEntity() {
        CustomerEntity entity = new CustomerEntity();
        entity.setId(ID);
        entity.setTgId(TG_ID);
        entity.setFirstName(FIRSTNAME);
        entity.setUserName(USERNAME);
        entity.setEmail(EMAIL);
        entity.setPasswordHash(PASSWORD_HASH);
        entity.setUuid(UUID);
        return entity;
    }
}
