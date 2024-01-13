package org.example.repositories;

import org.example.tables.Builds;
import org.example.tables.Champions;
import org.example.tables.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<Items, String> {
    Items findByItemName(String itemName);
    List<Items> findAll();
//    @Modifying
//    @Query("DELETE FROM Items i WHERE :build MEMBER OF i.builds")
//    void deleteBuildFromItems(@Param("build") Builds build);
}
