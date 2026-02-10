package com.mcn.in4.domain.health.controller;

import com.mcn.in4.domain.health.dto.HealthResponseDto.AssembledHealthInfo;
import com.mcn.in4.domain.health.dto.HealthResponseDto.HealthPresigned;
import com.mcn.in4.domain.health.entity.Health;
import com.mcn.in4.domain.health.service.HealthService;
import com.mcn.in4.domain.health.controller.api.ManageHealthApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/health/manage")
@RequiredArgsConstructor
public class HealthManageController implements ManageHealthApi {

    private final HealthService healthService;

    @GetMapping("/")
    public AssembledHealthInfo generateManageHealthInfo(){
        return healthService.generateManageHealthInfo();
    }

    @GetMapping("/search")
    public AssembledHealthInfo search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (name != null && startDate != null && endDate != null) {
            return healthService.findByNameAndPeriod(name, startDate, endDate);
        } else if (name != null) {
            return healthService.findByName(name);
        } else if (startDate != null && endDate != null) {
            return healthService.findByPeriod(startDate, endDate);
        } else {
            return healthService.findAll();
        }
    }

    @DeleteMapping("/delete/{healthID}")
    public HealthPresigned delete(
            @PathVariable(required = true) Long healthID){
        return healthService.deleteByHealthId(healthID);
    }
}