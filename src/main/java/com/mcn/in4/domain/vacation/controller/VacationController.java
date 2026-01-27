package com.mcn.in4.domain.vacation.controller;

import com.mcn.in4.domain.vacation.dto.request.VacationRequestDTO;
import com.mcn.in4.domain.vacation.dto.response.VacationResponseDTO;
import com.mcn.in4.domain.vacation.service.VacationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vacations")
@RequiredArgsConstructor
public class VacationController {

    private final VacationService vacationService;

    @PostMapping
    public ResponseEntity<VacationResponseDTO> createVacation(
            @RequestParam("type") String type,
            @RequestBody VacationRequestDTO request
    ) {
        VacationResponseDTO response = vacationService.createVacation(type, request);
        return ResponseEntity.ok(response);
    }
}
