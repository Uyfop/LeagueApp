package org.example.repositories;

import org.example.tables.Abilities;
import org.example.tables.Champions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AbilitiesRepository extends JpaRepository<Abilities, Long> {

    Abilities findById(long id);

    Abilities findByAbilityName(String abilityName);
    List<Abilities> findByChampionName(Champions champion);

    @Query("SELECT a FROM Abilities a WHERE a.abilityCD = :cooldown")
    List<Abilities> findAbilitiesByChampionAndCooldown(@Param("cooldown") int cooldown);

    void deleteByChampionName(Optional<Champions> champion);
}
