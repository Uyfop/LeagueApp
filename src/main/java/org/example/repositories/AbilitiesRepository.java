package org.example.repositories;

import org.example.tables.Abilities;
import org.example.tables.Champions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AbilitiesRepository extends JpaRepository<Abilities, Long> {

    List<Abilities> findByChampionName(Champions champion);

    void deleteByChampionName(Optional<Champions> champion);

    Abilities findById(long id);
}
