package org.example.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.example.services.ChampionsService;
import org.example.tables.Champions;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @DeleteMapping("/champions/{champName}")
    public ResponseEntity<?> deleteAbility(@PathVariable String champName) {
        boolean deleted = championsService.deleteChampionByName(champName);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    @PostMapping(value = "/champions")
    public ResponseEntity<Champions> createChampion(@Valid @RequestBody Champions champion) {
        Champions createdChampion = championsService.saveChampion(champion);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChampion);
    }

    @GetMapping(value = "/champions")
    public ResponseEntity<List<Champions>> getAllChampions() {
        List<Champions> champions = championsService.listAllChampions();
        return ResponseEntity.ok(champions);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
