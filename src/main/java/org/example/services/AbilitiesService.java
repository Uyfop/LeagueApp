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
        Optional<Abilities> existingAbility = Optional.ofNullable(abilitiesRepository.findByAbilityName((ability).getAbilityName()));
        if(existingAbility.isPresent())
            throw new IllegalArgumentException("The ability already exists");
        if (champion.isPresent() && checkRegexAbilityName(ability)) {
            ability.setChampionName(champion.get());
            return abilitiesRepository.save(ability);
        }
        throw new IllegalArgumentException("Invalid ability name format");
    }

    public List<Abilities> getAbilitiesByChampion(String champName) {
        Optional<Champions> champion = championsService.getChampionById(champName);
        if (champion.isPresent()) {
            return abilitiesRepository.findByChampionName(champion.get());
        }
        throw new IllegalArgumentException("No champion with that name");
    }

    public boolean deleteAbilityById(Long abilityId) {
        Optional<Abilities> ability = abilitiesRepository.findById(abilityId);
        if (ability.isPresent()) {
            abilitiesRepository.delete(ability.get());
            return true;
        } else {
            throw new IllegalArgumentException("Ability doesn't exist");
        }
    }
    public boolean deleteAbilityByName(String abilityName) {
        Optional<Abilities> ability = Optional.ofNullable(abilitiesRepository.findByAbilityName(abilityName));
        if (ability.isPresent()) {
            abilitiesRepository.delete(ability.get());
            return true;
        } else {
            throw new IllegalArgumentException("Ability doesn't exist");
        }
    }

    public List<Abilities> getAbilitiesByChampionAndCooldown(int cooldown) {
        return abilitiesRepository.findAbilitiesByChampionAndCooldown(cooldown);
    }

    public Optional<Abilities> updateAbility(Long abilityId, Abilities updatedAbility) {
        Optional<Abilities> existingAbility = abilitiesRepository.findById(abilityId);
        if(!existingAbility.isPresent())
            throw new IllegalArgumentException("Ability doesn't exist");
        return existingAbility.map(ability -> {
            ability.setAbilityName(updatedAbility.getAbilityName());
            ability.setAbilityCD(updatedAbility.getAbilityCD());
            ability.setAbilityDescription(updatedAbility.getAbilityDescription());
            ability.setAbilityCost(updatedAbility.getAbilityCost());
            return abilitiesRepository.save(ability);
        });
    }
    public boolean checkRegexAbilityName(Abilities ability) {
        String abilityName = ability.getAbilityName();
        String regexPattern = "^[A-Za-z]+$";
        return abilityName.matches(regexPattern);
    }
}
