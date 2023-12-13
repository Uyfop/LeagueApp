package org.example.services;

import org.example.tables.Champions;
import org.example.repositories.ChampionsRepository;
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
        championsRepository.save(champion);
        return champion;
    }
    public boolean deleteChampionByName(String champName){
        Champions champion = championsRepository.getReferenceById(champName);
        if(champion != null) {
            championsRepository.delete(champion);
            return true;
        }
        return false;
    }

}