package br.com.crm.beauty.web.dtos;

import jakarta.validation.constraints.NotEmpty;

import java.time.DayOfWeek;
import java.util.List;

public record WorkingDayDto(
        @NotEmpty(message = "At least one day must be provided")
        List<DayOfWeek> days,
        Long employeeId) {
}
