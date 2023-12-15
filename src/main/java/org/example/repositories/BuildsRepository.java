package org.example.repositories;

import org.example.tables.Abilities;
import org.example.tables.Builds;
import org.example.tables.Champions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BuildsRepository extends JpaRepository<Builds, Long> {
    Optional<Builds> findById(Long id);

   List<Builds> findAll();

    boolean existsByChampionChampName(String champName);

    Optional<Builds> findByChampionChampName(String champName);
}

