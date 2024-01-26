package org.example.services;

import org.example.tables.Abilities;
import org.example.tables.Champions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AbilitiesServiceIF  {
    Abilities addAbilityToChampion(String champName, Abilities ability);

    List<Abilities> getAbilitiesByChampion(String champName);

    boolean deleteAbilityById(Long abilityId);

    List<Abilities> getAbilitiesByChampionAndCooldown(int cooldown);
    Optional<Abilities> updateAbilityByName(String abilityName, Abilities updatedAbility);
    Optional<Abilities> updateAbility(Long abilityId, Abilities updatedAbility);
    boolean checkRegexAbilityName(Abilities ability);
    boolean deleteAbilityByName(String abilityName);
    Page<Abilities> listAllAbilitiesWithPagination(Pageable pageable);
}
