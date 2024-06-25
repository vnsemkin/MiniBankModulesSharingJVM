package org.vnsemkin.semkinmiddleservice.application.dtos.front;

import org.springframework.lang.NonNull;

public record TransferRequest(@NonNull String from, @NonNull String to, @NonNull String amount) {
}