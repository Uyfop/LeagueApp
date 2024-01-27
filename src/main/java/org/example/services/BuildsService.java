package org.example.services;
import javax.persistence.EntityNotFoundException;
import org.example.repositories.BuildsRepository;
import org.example.repositories.ChampionsRepository;
import org.example.repositories.ItemsRepository;
import org.example.tables.Abilities;
import org.example.tables.Builds;
import org.example.tables.Champions;
import org.example.tables.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BuildsService implements BuildsServiceIF{

    private final BuildsRepository buildsRepository;
    private final ChampionsRepository championsRepository;
    private final ItemsRepository itemsRepository;

    @Autowired
    public BuildsService(BuildsRepository buildsRepository, ChampionsRepository championsRepository, ItemsRepository itemsRepository) {
        this.buildsRepository = buildsRepository;
        this.championsRepository = championsRepository;
        this.itemsRepository = itemsRepository;
    }
    public Optional<Builds> GetBuildById(Long id){
        return buildsRepository.findById(id);
    }
    public List<Builds> ListAllBuilds(){
        return buildsRepository.findAll();
    }

    public Optional<Builds> getBuildForChampion(String champName) {
        return buildsRepository.findByChampionChampName(champName);
    }

    public Builds saveBuild(String champName, List<String> itemNames, Builds build) {
        Optional<Champions> championOptional = championsRepository.findById(champName);

        if (championOptional.isPresent()) {
            Champions champion = championOptional.get();

            if (getBuildsByChampion(champName)) {
                throw new IllegalArgumentException("Champion already has a build.");
            }

            build.setChampion(champion);

            List<Items> items = itemNames.stream()
                    .map(itemName -> itemsRepository.findById(itemName).orElseThrow(EntityNotFoundException::new))
                    .collect(Collectors.toList());
            build.setItems(items);

            ZonedDateTime now = ZonedDateTime.now();
            build.setCreationDate(now);

            return buildsRepository.save(build);
        } else {
            throw new IllegalArgumentException("Champion not found");
        }
    }

    public boolean deleteBuildById(Long buildId) {
        Optional<Builds> build = buildsRepository.findById(buildId);

        if (build.isPresent()) {

            buildsRepository.delete(build.get());
            return true;
        } else {
            return false;
        }
    }
    public boolean getBuildsByChampion(String champName) {
        return buildsRepository.existsByChampionChampName(champName);
    }

    public Optional<Builds> findBuildByChampion(String champName){
        return buildsRepository.findByChampionChampName(champName);
    }

    public Builds updateBuild(Long buildId, List<String> itemNames, Builds updatedBuild) {
        Builds existingBuild = buildsRepository.findById(buildId).orElseThrow(EntityNotFoundException::new);

        updatedBuild.setChampion(existingBuild.getChampion());

        List<Items> items = itemNames.stream()
                .map(itemName -> itemsRepository.findById(itemName).orElseThrow(EntityNotFoundException::new))
                .collect(Collectors.toList());
        existingBuild.setItems(items);

        return buildsRepository.save(existingBuild);
    }


    public List<Builds> findBuildsByItemName(String itemName) {
        return buildsRepository.findBuildsByItemName(itemName);
    }

    public Page<Builds> listAllBuilds(Pageable pageable) {
        return buildsRepository.findAll(pageable);
    }
}
