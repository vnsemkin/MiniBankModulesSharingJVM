package org.vnsemkin.semkinmiddleservice.application.dtos.front;

import org.springframework.lang.NonNull;

public record CustomerInfoResponse(@NonNull String firstName, @NonNull String userName, @NonNull String email,
                                   @NonNull String uuid, @NonNull String accountName) {
}
