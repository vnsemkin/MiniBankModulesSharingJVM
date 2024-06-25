package org.vnsemkin.semkinmiddleservice.application.dtos.back;

import org.springframework.lang.NonNull;

import java.math.BigDecimal;

public record AccountInfoResponse(@NonNull String accountId,
                                  @NonNull String accountName,
                                  @NonNull BigDecimal balance) {
}