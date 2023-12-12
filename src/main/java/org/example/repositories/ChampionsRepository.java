package org.example.repositories;

import org.example.tables.Champions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChampionsRepository extends JpaRepository<Champions, String> {

    @Override
    Optional<Champions> findById(String champName);

    List<Champions> findAll();

    void deleteByChampName(String champName);

    boolean existsByChampName(String champName);

    Page<Champions> findAll(Pageable pageable);

//    Champions update(String champName, Champions champion);
//    @Modifying
//    @Query("UPDATE Champion c SET c.champName = :champName WHERE c.champName = :newChampName")
//    void update(@Param("champName") String champName, @Param("newChampName") String newChampName);
}
