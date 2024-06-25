package org.vnsemkin.semkinmiddleservice.domain.models;

public record ErrorModel(String message, String type, String code, String trace_id) {
}
