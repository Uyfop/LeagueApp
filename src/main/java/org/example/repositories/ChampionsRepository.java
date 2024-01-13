package org.example.repositories;

import org.example.tables.Champions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChampionsRepository extends JpaRepository<Champions, String> {

    Optional<Champions> findById(String champName);
    List<Champions> findAll();

}
