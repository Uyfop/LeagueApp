package org.example.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.services.ChampionsService;
import org.example.tables.Champions;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ChampionController {
    private final ChampionsService championsService;

    public ChampionController(ChampionsService championsService) {
        this.championsService = championsService;
    }

    @GetMapping(value = "/champions/{champName}")
    public ResponseEntity<Champions> getChampionById(@PathVariable String champName) {
        Optional<Champions> champion = championsService.getChampionById(champName);
        return champion.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    //tutaj dodac validator

    @PostMapping(value = "/champions")
    public ResponseEntity<Champions> createChampion(@RequestBody Champions champion) {
        Champions createdChampion = championsService.saveChampion(champion);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChampion);
    }

//    @PutMapping("/{champName}")
//    public ResponseEntity<Champions> updateChampion(@PathVariable String champName, @RequestBody Champions champion) {
//        Champions updatedChampion = championsService.updateChampionName(champName, champion.getChampName());
//        return updatedChampion != null ? ResponseEntity.ok(updatedChampion) : ResponseEntity.notFound().build();
//    }

    @DeleteMapping(value = "/champions/{champName}")
    public ResponseEntity<?> deleteChampion(@PathVariable String champName) {
        boolean deleted = championsService.deleteChampionByName(champName);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/champions")
    public ResponseEntity<List<Champions>> getAllChampions() {
        List<Champions> champions = championsService.listAllChampions();
        return ResponseEntity.ok(champions);
    }
}
