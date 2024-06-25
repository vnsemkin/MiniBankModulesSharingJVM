package org.vnsemkin.gbpbackend.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vnsemkin.gbpbackend.application.dtos.ErrorModelDto;
import org.vnsemkin.gbpbackend.application.dtos.UserCreationReq;
import org.vnsemkin.gbpbackend.application.dtos.UuidRespDto;
import org.vnsemkin.gbpbackend.domain.model.AccountName;

import java.math.BigDecimal;
import java.util.UUID;

@RestController()
@RequestMapping("/v2/")
public class BackendController {
    private static final String MESSAGE = "Произошло что-то ужасное, но станет лучше, честно";
    private static final String ACCOUNT_NAME = "Test";
    private static final String TYPE = "GeneralError";
    private static final String CODE = "123";
    private static final String TRACE_ID = "5f59e024-03c7-498d-9fc9-b8b15fd49c47";
    private static final BigDecimal AMOUNT = new BigDecimal("0.00");
    private static final long FROM = 217647263L;
    private static final long TO = 98798798L;
    private String accountName;

    @PostMapping("users")
    public ResponseEntity<?> createUser(@RequestBody UserCreationReq req) {
        System.out.println(req);
        return ResponseEntity.noContent().build();
    }

    private ErrorModelDto getErrorModel() {
        return new ErrorModelDto(MESSAGE, TYPE, CODE, TRACE_ID);
    }

    public record AccountResponse(String accountId, String accountName, BigDecimal balance) {
    }

    public record TransferRequest(String from, String to, String amount) {
    }

    public record TransferResponse(String transferId) {
    }


    @GetMapping("users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable long id) {
        System.out.println(id);
        UUID userId = UUID.randomUUID();
        return ResponseEntity.ok().body(new UuidRespDto(userId.toString()));
    }

    @PostMapping("users/{id}/accounts")
    public ResponseEntity<?> createAccount(@PathVariable long id
        , @RequestBody AccountName accountName) {
        this.accountName = accountName.getAccountName();
        System.out.println("Id: " + id + "Account name" + accountName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("users/{id}/accounts")
    public ResponseEntity<?> getAccount(@PathVariable long id) {
        UUID uuid = UUID.randomUUID();
        AccountResponse accountResponse = new AccountResponse(uuid.toString(), accountName, AMOUNT);
        System.out.println(accountResponse);
        return ResponseEntity.ok()
            .body(accountResponse);
    }

    @PostMapping("/transfers")
    public ResponseEntity<?> transferMoney(@RequestBody TransferRequest transferRequest) {
        System.out.println(transferRequest);
        return ResponseEntity.ok().body(new TransferResponse(UUID.randomUUID().toString()));
    }
}
