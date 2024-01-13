package org.example.controllers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.example.services.ChampionsService;
import org.example.tables.Champions;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
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

    @GetMapping(value = "/champions/all")
    public ResponseEntity<List<Champions>> getAllChampions() {
        List<Champions> champions = championsService.listAllChampions();
        return ResponseEntity.ok(champions);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
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


    @PutMapping("/champions/{champName}")
    public ResponseEntity<Champions> updateChampion(@PathVariable String champName, @Valid @RequestBody Champions updatedChampion) {
        Optional<Champions> result = championsService.updateChampion(champName, updatedChampion);

        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping(value = "/champions")
    public ResponseEntity<Page<Champions>> getAllChampionsWithPagination(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "1") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Champions> championsPage = championsService.listAllChampionsWithPagination(pageable);
        return ResponseEntity.ok(championsPage);
    }

}
