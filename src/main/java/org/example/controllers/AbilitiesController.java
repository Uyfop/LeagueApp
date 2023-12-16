package org.example.controllers;

import org.example.services.AbilitiesService;
import org.example.tables.Abilities;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.example.tables.Items;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AbilitiesController {
    private final AbilitiesService abilitiesService;

    public AbilitiesController(AbilitiesService abilitiesService) {
        this.abilitiesService = abilitiesService;
    }

    @PostMapping("/abilities/{championName}")
    public ResponseEntity<Abilities> createAbility(@PathVariable String championName, @RequestBody Abilities ability) {
        Abilities createdAbility = abilitiesService.addAbilityToChampion(championName, ability);
        if (createdAbility != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAbility);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/abilities/id/{abilityId}")
    public ResponseEntity<?> deleteAbilityById(@PathVariable Long abilityId) {
        boolean deleted = abilitiesService.deleteAbilityById(abilityId);
        if (deleted) {
            return ResponseEntity.ok("Ability deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/abilities/abilityname/{abilityName}")
    public ResponseEntity<?> deleteAbilityByName(@PathVariable String abilityName) {
        boolean deleted = abilitiesService.deleteAbilityByName(abilityName);
        if (deleted) {
            return ResponseEntity.ok("Ability deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/abilities/{championName}")
    public ResponseEntity<List<Abilities>> getAbilitiesByChampion(@PathVariable String championName) {
        List<Abilities> abilities = abilitiesService.getAbilitiesByChampion(championName);
        if (abilities != null && !abilities.isEmpty()) {
            return ResponseEntity.ok(abilities);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/abilities/abilityByCD")
    public ResponseEntity<List<Abilities>> getAbilitiesByCustomQuery(
            @RequestParam int cooldown
    ) {
        List<Abilities> abilities = abilitiesService.getAbilitiesByChampionAndCooldown(cooldown);
        if (!abilities.isEmpty()) {
            return ResponseEntity.ok(abilities);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/abilities/{abilityID}")
    public ResponseEntity<Abilities> updateAbility(@PathVariable Long abilityID, @Valid @RequestBody Abilities updatedAbility) {
        Optional<Abilities> result = abilitiesService.updateAbility(abilityID,updatedAbility);
        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}
