package org.vnsemkin.gbpbackend.application.dtos;
public record ErrorModelDto(String message, String type, String code, String trace_id) {
}