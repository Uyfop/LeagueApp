package org.example.services;

import org.example.tables.Abilities;
import org.example.tables.Champions;

import java.util.List;
import java.util.Optional;

public interface AbilitiesServiceIF  {
    Abilities addAbilityToChampion(String champName, Abilities ability);

    List<Abilities> getAbilitiesByChampion(String champName);

    boolean deleteAbilityById(Long abilityId);

    List<Abilities> getAbilitiesByChampionAndCooldown(int cooldown);
}
