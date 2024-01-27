package org.example.repositories;

import org.example.tables.Abilities;
import org.example.tables.Builds;
import org.example.tables.Champions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BuildsRepository extends JpaRepository<Builds, Long> {
    Optional<Builds> findById(Long id);

    List<Builds> findAll();

    void deleteById(Long id);

    boolean existsByChampionChampName(String champName);

    Optional<Builds> findByChampionChampName(String champName);

    @Query("SELECT b FROM Builds b JOIN b.items i WHERE i.itemName = :itemName")
    List<Builds> findBuildsByItemName(@Param("itemName") String itemName);


}


