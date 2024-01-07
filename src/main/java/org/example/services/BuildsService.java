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
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BuildsService implements BuildsServiceIF{

    private final BuildsRepository buildsRepository;

    private final ChampionsService championsService;
    private final ChampionsRepository championsRepository;
    private final ItemsRepository itemsRepository;
    private final ItemService itemService;
    @Autowired
    public BuildsService(BuildsRepository buildsRepository, ChampionsService championsService, ItemService itemService, ChampionsRepository championsRepository, ItemsRepository itemsRepository) {
        this.buildsRepository = buildsRepository;
        this.championsService = championsService;
        this.itemService = itemService;
        this.championsRepository = championsRepository;
        this.itemsRepository = itemsRepository;
    }
    public Optional<Builds> GetBuildById(Long id){
        return buildsRepository.findById(id);
    }
    public List<Builds> ListAllBuilds(){
        return buildsRepository.findAll();
    }
    public Builds saveBuild(String champName, List<String> itemNames, Builds build) {
        Optional<Champions> championOptional = championsService.getChampionById(champName);
        List<Items> items = new ArrayList<>();

        if (championOptional.isPresent()) {
            Champions champion = championOptional.get();
            build.setChampion(champion);

            if (itemNames != null) {
                for (String itemName : itemNames) {
                    Items item = itemService.GetItemByName(itemName);
                    if (item != null) {
                        items.add(item);
                    }
                }
            }
            build.setItems(items);
            ZonedDateTime now = ZonedDateTime.now();
            build.setCreationDate(now);

            if (build.getChampion() != null) {
                return buildsRepository.save(build);
            } else {
                throw new IllegalArgumentException("Champion not set in the build");
            }
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

    public Builds updateBuild(Long buildId, String championName, List<String> itemNames, Builds updatedBuild) {
        Builds existingBuild = buildsRepository.findById(buildId).orElseThrow(EntityNotFoundException::new);

        Champions champion = championsRepository.findById(championName).orElseThrow(EntityNotFoundException::new);
        existingBuild.setChampion(champion);

        List<Items> items = itemNames.stream()
                .map(itemName -> itemsRepository.findById(itemName).orElseThrow(EntityNotFoundException::new))
                .collect(Collectors.toList());
        existingBuild.setItems(items);

        return buildsRepository.save(existingBuild);
    }



    public List<Builds> findBuildsByItemName(String itemName) {
        return buildsRepository.findBuildsByItemName(itemName);
    }
//    public void disassociateChampion(Champions champion) {
//        if (champion != null) {
//            champion.setBuild(null);
//        }
//    }
//
//    public void disassociateItems(List<Items> items) {
//        for (Items item : items) {
//            item.getBuilds().clear();
//        }
//    }
}
