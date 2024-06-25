package org.vnsemkin.semkinmiddleservice.domain.models;

import org.springframework.lang.NonNull;

import java.math.BigDecimal;

public record Account(long id, @NonNull String uuid, @NonNull String accountName, @NonNull BigDecimal balance) {
}