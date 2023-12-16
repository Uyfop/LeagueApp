package org.example.repositories;

import org.example.tables.Champions;
import org.example.tables.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<Items, String> {
    Items findByItemName(String itemName);
    List<Items> findAll();

}
