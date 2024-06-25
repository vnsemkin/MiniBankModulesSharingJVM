package org.vnsemkin.semkinmiddleservice.application.dtos.front;

import org.springframework.lang.NonNull;

public record CustomerRegistrationResponse(@NonNull String firstName, @NonNull String email) {
}