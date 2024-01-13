package org.example.services;

import org.example.tables.Champions;
import org.example.repositories.ChampionsRepository;
import org.example.tables.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChampionsService implements ChampionsServiceIF{

    private final ChampionsRepository championsRepository;

    @Autowired
    public ChampionsService(ChampionsRepository championsRepository) {
        this.championsRepository = championsRepository;
    }

    public Optional<Champions> getChampionById(String champName) {
        return championsRepository.findById(champName);
    }

    public List<Champions> listAllChampions(){
        return championsRepository.findAll();
    }

    public Champions saveChampion(Champions champion) {
        Optional<Champions> existingChampion = championsRepository.findById(champion.getChampName());
        if(existingChampion.isPresent())
            throw new IllegalArgumentException("The champion already in the database");
        if (checkRegexChampName(champion)) {
            return championsRepository.save(champion);
        } else {
            throw new IllegalArgumentException("Invalid champion name format");
        }
    }
    public boolean deleteChampionByName(String champName){
        Champions champion = championsRepository.getReferenceById(champName);
        if(champion != null) {
            championsRepository.delete(champion);
            return true;
        }
        throw new IllegalArgumentException("The champion doesnt exists");
    }
    public Optional<Champions> updateChampion(String champName, Champions updatedChampion) {
        return championsRepository.findById(champName)
                .map(champion -> {
                    if (!champName.equals(updatedChampion.getChampName())) {
                        throw new IllegalArgumentException("champion name cannot be changed.");
                    }
                    champion.setChampType(updatedChampion.getChampType());
                    return championsRepository.save(champion);
                });
    }
    public boolean checkRegexChampName(Champions champion) {
        String champName = champion.getChampName();
        String regexPattern = "^[A-Za-z]+$";
        return champName.matches(regexPattern);
    }

    public Page<Champions> listAllChampionsWithPagination(Pageable pageable) {
        return championsRepository.findAll(pageable);
    }

}