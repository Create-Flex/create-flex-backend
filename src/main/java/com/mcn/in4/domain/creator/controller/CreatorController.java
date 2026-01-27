package com.mcn.in4.domain.creator.controller;

import com.mcn.in4.domain.creator.dto.CreatorDto;
import com.mcn.in4.domain.creator.service.CreatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creators")
@RequiredArgsConstructor
public class CreatorController {

    private final CreatorService creatorService;

    @PostMapping
    public ResponseEntity<Long> createCreator(
            @RequestParam("member_name") String memberName,
            @RequestParam("creator_platform") String creatorPlatform,
            @RequestParam("creator_subscribe") String creatorSubscribe,
            @RequestParam("creator_category") String creatorCategory,
            @RequestParam("member_account") String memberAccount,
            @RequestParam("member_password") String memberPassword,
            @RequestParam("member_manager_id") Long memberManagerId,
            @RequestParam("creator_status") String creatorStatus) {

        Long creatorId = creatorService.createCreator(
                memberName, creatorPlatform, creatorSubscribe, creatorCategory,
                memberAccount, memberPassword, memberManagerId, creatorStatus);
        return ResponseEntity.ok(creatorId);
    }

    @GetMapping
    public ResponseEntity<List<CreatorDto.Response>> getAllCreators() {
        List<CreatorDto.Response> creators = creatorService.getAllCreators();
        return ResponseEntity.ok(creators);
    }

    @GetMapping("/{creatorId}")
    public ResponseEntity<CreatorDto.Response> getCreatorById(@PathVariable Long creatorId) {
        CreatorDto.Response response = creatorService.getCreatorById(creatorId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{creatorId}")
    public ResponseEntity<CreatorDto.Response> updateCreator(
            @PathVariable Long creatorId,
            @RequestParam(value = "member_name", required = false) String memberName,
            @RequestParam(value = "creator_platform", required = false) String creatorPlatform,
            @RequestParam(value = "creator_subscribe", required = false) String creatorSubscribe,
            @RequestParam(value = "creator_category", required = false) String creatorCategory,
            @RequestParam(value = "member_account", required = false) String memberAccount,
            @RequestParam(value = "member_password", required = false) String memberPassword,
            @RequestParam(value = "member_manager_id", required = false) Long memberManagerId,
            @RequestParam(value = "creator_status", required = false) String creatorStatus) {

        CreatorDto.Response response = creatorService.updateCreator(
                creatorId, memberName, creatorPlatform, creatorSubscribe, creatorCategory,
                memberAccount, memberPassword, memberManagerId, creatorStatus);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{creatorId}")
    public ResponseEntity<Void> deleteCreator(@PathVariable Long creatorId) {
        creatorService.deleteCreator(creatorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/manager/me")
    public ResponseEntity<List<CreatorDto.Response>> getMyCreators(
            @RequestParam("manager_id") Long managerId) {
        List<CreatorDto.Response> creators = creatorService.getMyCreators(managerId);
        return ResponseEntity.ok(creators);
    }
}