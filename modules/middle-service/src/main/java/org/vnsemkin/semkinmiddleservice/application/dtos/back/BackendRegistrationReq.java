package org.vnsemkin.semkinmiddleservice.application.dtos.back;

import org.springframework.lang.NonNull;

public record BackendRegistrationReq(long userId,
                                     @NonNull String userName) {
}