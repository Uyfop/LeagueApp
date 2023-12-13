package org.example.services;

import org.example.tables.Abilities;
import org.example.tables.Champions;
import org.example.repositories.AbilitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AbilitiesService implements AbilitiesServiceIF{
    private final AbilitiesRepository abilitiesRepository;
    private final ChampionsService championsService;

    @Autowired
    public AbilitiesService(AbilitiesRepository abilitiesRepository, ChampionsService championsService) {
        this.abilitiesRepository = abilitiesRepository;
        this.championsService = championsService;
    }

    public Abilities addAbilityToChampion(String champName, Abilities ability) {
        Optional<Champions> champion = championsService.getChampionById(champName);
        if (champion.isPresent()) {
            ability.setChampionName(champion.get());
            return abilitiesRepository.save(ability);
        }
        return null;
    }

    public List<Abilities> getAbilitiesByChampion(String champName) {
        Optional<Champions> champion = championsService.getChampionById(champName);
        if (champion.isPresent()) {
            return abilitiesRepository.findByChampionName(champion.get());
        }
        return null;
    }

    public boolean deleteAbilityById(Long abilityId) {
        Optional<Abilities> ability = abilitiesRepository.findById(abilityId);
        if (ability.isPresent()) {
            abilitiesRepository.delete(ability.get());
            return true;
        }
        return false;
    }

    public List<Abilities> getAbilitiesByChampionAndCooldown(int cooldown) {
        return abilitiesRepository.findAbilitiesByChampionAndCooldown(cooldown);
    }

}
