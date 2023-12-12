package org.example.controllers;

import org.example.services.AbilitiesService;
import org.example.tables.Abilities;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/abilities/{abilityId}")
    public ResponseEntity<?> deleteAbility(@PathVariable Long abilityId) {
        boolean deleted = abilitiesService.deleteAbilityById(abilityId);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
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

}
