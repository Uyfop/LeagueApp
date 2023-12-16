package org.example.controllers;

import org.example.services.BuildsService;
import org.example.tables.Abilities;
import org.example.tables.Builds;
import org.example.tables.Items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class BuildsController{

    private final BuildsService buildsService;

    @Autowired
    public BuildsController(BuildsService buildsService) {
        this.buildsService = buildsService;
    }

    @GetMapping("/builds/{id}")
    public ResponseEntity<Builds> getBuildById(@PathVariable Long id) {
        Optional<Builds> build = buildsService.GetBuildById(id);
        return build.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/builds/champion/{champName}")
    public ResponseEntity<Builds> findBuildByChampion(@PathVariable String champName ) {
        Optional<Builds> build =  buildsService.findBuildByChampion(champName);
        return build.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/builds/all")
    public ResponseEntity<List<Builds>> getAllBuilds() {
        List<Builds> builds = buildsService.ListAllBuilds();
        return ResponseEntity.ok(builds);
    }

    @PostMapping("/builds/save")
    public ResponseEntity<Builds> saveBuild(@Valid @RequestBody Builds build) {
        List<String> itemNames = build.getItems().stream()
                .map(Items::getItemName)
                .collect(Collectors.toList());
        Builds savedBuild = buildsService.saveBuild(build.getChampion().getChampName(), itemNames, build);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBuild);
    }

    @DeleteMapping("/builds/{id}")
    public ResponseEntity<Void> deleteBuildById(@PathVariable Long id) {
        boolean deleted = buildsService.deleteBuildById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/builds/{id}")
    public ResponseEntity<Builds> updateBuild(@PathVariable Long id, @Valid @RequestBody Builds updatedBuild) {
        Optional<Builds> result = buildsService.updateBuild(id,updatedBuild);
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
