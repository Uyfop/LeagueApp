package org.example.controllers;

import org.example.services.BuildsService;
import org.example.tables.Builds;
import org.example.tables.Items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<Builds> saveBuild(@RequestBody Builds build) {
        List<String> itemNames = build.getItems().stream()
                .map(Items::getItemName)
                .collect(Collectors.toList());
        Builds savedBuild = buildsService.saveBuild(build.getChampion().getChampName(), itemNames, build);
        if (savedBuild != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBuild);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/builds/{id}")
    public ResponseEntity<Void> deleteBuildById(@PathVariable Long id) {
        boolean deleted = buildsService.deleteBuildByID(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
