package org.vnsemkin.semkinmiddleservice.application.dtos.back;

import org.springframework.lang.NonNull;

public record AccountRegistrationReq(long id, @NonNull String accountName) {
}