package org.example.services;

import org.example.tables.Champions;
import java.util.List;
import java.util.Optional;

public interface ChampionsServiceIF {

    Optional<Champions> getChampionById(String champName);

    List<Champions> listAllChampions();

    Champions saveChampion(Champions champion);

    boolean deleteChampionByName(String champName);
    Optional<Champions> updateChampion(String champName, Champions updatedChampion);
}
