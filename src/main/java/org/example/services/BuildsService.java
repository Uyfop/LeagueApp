package org.example.services;

import org.example.repositories.BuildsRepository;
import org.example.tables.Abilities;
import org.example.tables.Builds;
import org.example.tables.Champions;
import org.example.tables.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class BuildsService implements BuildsServiceIF{

    private final BuildsRepository buildsRepository;

    private final ChampionsService championsService;

    private final ItemService itemService;
    @Autowired
    public BuildsService(BuildsRepository buildsRepository, ChampionsService championsService, ItemService itemService) {
        this.buildsRepository = buildsRepository;
        this.championsService = championsService;
        this.itemService = itemService;
    }
    public Optional<Builds> GetBuildById(Long id){
        return buildsRepository.findById(id);
    }
    public List<Builds> ListAllBuilds(){
        return buildsRepository.findAll();
    }
    public Builds saveBuild(String champname, List<String> itemnames, Builds build){
        Optional<Champions> champion = championsService.getChampionById(champname);
        List<Items> items = new ArrayList<>();
        if (itemnames != null) {
            for (int i=0; i<itemnames.size(); i++) {
                Items item = itemService.GetItemByName(itemnames.get(i));
                if (item != null) {
                    items.add(item);
                }
            }
        }
        if (champion.isPresent() && !buildsRepository.existsByChampionChampName(champion.get().getChampName())) {
            build.setChampion(champion.get());
            build.setItems(items);
            ZonedDateTime now = ZonedDateTime.now();
            build.setCreationDate(now);
            return buildsRepository.save(build);
        }
        return null;
    }

    public boolean deleteBuildByID(Long id){
        Builds build = buildsRepository.getReferenceById(id);
        if(build != null) {
            buildsRepository.delete(build);
            return true;
        }
        return false;
    }

    public boolean getBuildsByChampion(String champName) {
        return buildsRepository.existsByChampionChampName(champName);
    }

    public Optional<Builds> findBuildByChampion(String champName){
        return buildsRepository.findByChampionChampName(champName);
    }
}
