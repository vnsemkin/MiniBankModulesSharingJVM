package org.vnsemkin.semkinmiddleservice.application.dtos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.vnsemkin.semkinmiddleservice.application.dtos.back.BackendRespUuid;
import org.vnsemkin.semkinmiddleservice.presentation.exception.UuidValidationException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BackendRespUuidTest {

    @Test
    void shouldThrowExceptionOnInvalidUuid() {
        String invalidUuid = "invalidUuid";
        assertThrows(UuidValidationException.class,
            ()-> new BackendRespUuid(invalidUuid));
    }

    @Test
    void shouldDoesntThrowExceptionOnValidUuid(){
        String validUuid = "123e4567-e89b-12d3-a456-426614174000";
        assertDoesNotThrow(()-> new BackendRespUuid(validUuid));
    }
}
