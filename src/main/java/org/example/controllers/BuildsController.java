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
    public ResponseEntity<?> saveBuild(@Valid @RequestBody Builds build) {
        try {
            if (build.getChampion() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Champion is required for saving the build.");
            }

            System.out.println("Champion details: " + build.getChampion().toString());

            List<String> itemNames = build.getItems().stream()
                    .map(Items::getItemName)
                    .collect(Collectors.toList());

            Builds savedBuild = buildsService.saveBuild(build.getChampion().getChampName(), itemNames, build);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBuild);
        } catch (NullPointerException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while processing the request.");
        }
    }


    @DeleteMapping("/builds/{buildId}")
    public ResponseEntity<Void> deleteBuild(@PathVariable Long buildId) {
        boolean deleted = buildsService.deleteBuildById(buildId);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/builds/update/{id}")
    public ResponseEntity<Builds> updateBuild(@PathVariable Long id, @Valid @RequestBody Builds updatedBuild) {
        Optional<Builds> existingBuildOptional = buildsService.GetBuildById(id);
        if (existingBuildOptional.isPresent()) {
            Builds existingBuild = existingBuildOptional.get();
            updatedBuild.setChampion(existingBuild.getChampion());
            Builds result = buildsService.updateBuild(existingBuild.getId(),
                    updatedBuild.getItems().stream()
                            .map(Items::getItemName)
                            .collect(Collectors.toList()),
                    updatedBuild);

            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/builds/item/{itemName}")
    public ResponseEntity<List<Builds>> findBuildsByItemName(@PathVariable String itemName) {
        List<Builds> builds = buildsService.findBuildsByItemName(itemName);
        return ResponseEntity.ok(builds);
    }

}
