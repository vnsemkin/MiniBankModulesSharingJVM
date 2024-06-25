package org.vnsemkin.semkinmiddleservice.application.usecases;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.TransferRequest;
import org.vnsemkin.semkinmiddleservice.application.dtos.front.TransferResponse;
import org.vnsemkin.semkinmiddleservice.application.external.BackendClientInterface;
import org.vnsemkin.semkinmiddleservice.application.mappers.AppMapper;
import org.vnsemkin.semkinmiddleservice.application.repositories.CustomerRepository;
import org.vnsemkin.semkinmiddleservice.application.repositories.TransactionRepository;
import org.vnsemkin.semkinmiddleservice.domain.models.Result;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.AccountEntity;
import org.vnsemkin.semkinmiddleservice.infrastructure.entities.CustomerEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransferMoneyServiceTest {
    static final String NOT_ENOUGH_MONEY = "Недостаточно денег не счету.";
    static final long ID = 123L;
    static long TG_ID = 32382763L;
    static final String FIRSTNAME = "Ivan";
    static final String STRING_STUB = "Stub";
    static final String USERNAME = "Ivanov";
    static final String EMAIL = "ivanov@test.ru";
    static final String PASSWORD_HASH = "dsgdhskdj";
    static final String UUID = "123e4567-e89b-12d3-a456-426614174000";
    static final BigDecimal BALANCE_FIVE_HUNDREDS = new BigDecimal("500.00");
    static final BigDecimal BALANCE_ONE_HUNDREDS = new BigDecimal("100.00");
    static final BigDecimal BALANCE_ZERO = new BigDecimal("0.00");
    static final String DEFAULT_ACCOUNT_NAME = "Акционный";
    @Mock
    CustomerRepository customerRepository;
    @Mock
    TransactionRepository transactionRepository;
    @Mock
    BackendClientInterface backendClientInterface;
    @InjectMocks
    TransferMoneyService transferMoneyService;
    AppMapper mapper = AppMapper.INSTANCE;

    @Test
    void shouldReturnNotEnoughMoneyOnLowBalance() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(ID);
        accountEntity.setAccountName(DEFAULT_ACCOUNT_NAME);
        accountEntity.setBalance(BALANCE_ZERO);
        CustomerEntity customerEntity = getCustomerEntity();
        customerEntity.setAccount(accountEntity);
        TransferRequest request =
            new TransferRequest(STRING_STUB, STRING_STUB, BALANCE_FIVE_HUNDREDS.toString());
        when(customerRepository.findByUserName(anyString())).thenReturn(Optional.of(customerEntity));

        Result<TransferResponse, String> response =
            transferMoneyService.transferMoney(request);

        assertTrue(response.isError());
        assertEquals(NOT_ENOUGH_MONEY, response.getError().get());
    }

    @Test
    void shouldReturnNotEnoughMoneyOnBalanceNull() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(ID);
        accountEntity.setAccountName(DEFAULT_ACCOUNT_NAME);
        accountEntity.setBalance(null);
        CustomerEntity customerEntity = getCustomerEntity();
        customerEntity.setAccount(accountEntity);
        TransferRequest request =
            new TransferRequest(STRING_STUB, STRING_STUB, BALANCE_FIVE_HUNDREDS.toString());
        when(customerRepository.findByUserName(anyString())).thenReturn(Optional.of(customerEntity));

        Result<TransferResponse, String> response =
            transferMoneyService.transferMoney(request);

        assertTrue(response.isError());
        assertEquals(NOT_ENOUGH_MONEY, response.getError().get());
    }

    @Test
    void shouldReturnNotEnoughMoneyOnZeroBalance() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(ID);
        accountEntity.setAccountName(DEFAULT_ACCOUNT_NAME);
        accountEntity.setBalance(BALANCE_ZERO);
        CustomerEntity customerEntity = getCustomerEntity();
        customerEntity.setAccount(accountEntity);
        TransferRequest request =
            new TransferRequest(STRING_STUB, STRING_STUB, BALANCE_ZERO.toString());
        when(customerRepository.findByUserName(anyString())).thenReturn(Optional.of(customerEntity));

        Result<TransferResponse, String> response =
            transferMoneyService.transferMoney(request);

        assertTrue(response.isError());
        assertEquals(NOT_ENOUGH_MONEY, response.getError().get());
    }

    @Test
    void shouldReturnSuccessOnTransferMoney() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(ID);
        accountEntity.setAccountName(DEFAULT_ACCOUNT_NAME);
        accountEntity.setBalance(BALANCE_FIVE_HUNDREDS);
        CustomerEntity customerEntity = getCustomerEntity();
        customerEntity.setAccount(accountEntity);
        TransferRequest request =
            new TransferRequest(STRING_STUB, STRING_STUB, BALANCE_ONE_HUNDREDS.toString());
        when(customerRepository.findByUserName(anyString())).thenReturn(Optional.of(customerEntity));
        when(backendClientInterface.transferMoney(request))
            .thenReturn(Result.success(new TransferResponse(UUID)));
        Result<TransferResponse, String> response =
            transferMoneyService.transferMoney(request);

        assertTrue(response.isSuccess());
        assertEquals(response.getData().get().transferId(), UUID);
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
