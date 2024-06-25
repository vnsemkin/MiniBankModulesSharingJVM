package org.vnsemkin.semkinmiddleservice.application.dtos.front;

import org.springframework.lang.NonNull;

import java.math.BigDecimal;

public record AccountRegistrationResponse(@NonNull String accountName, @NonNull BigDecimal balance,
                                          @NonNull String info) {
}