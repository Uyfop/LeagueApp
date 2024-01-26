package org.example.services;

import org.example.tables.Builds;
import org.example.tables.Items;
import org.example.tables.Champions;

import java.util.List;
import java.util.Optional;

public interface BuildsServiceIF {

    Optional<Builds> GetBuildById(Long id);
    List<Builds> ListAllBuilds();
    Builds saveBuild(String champname, List<String> itemnames, Builds build);
    boolean deleteBuildById(Long id);
    boolean getBuildsByChampion(String champName);
    Optional<Builds> findBuildByChampion(String champName);
    Builds updateBuild(Long buildId, List<String> itemNames, Builds updatedBuild);
    List<Builds> findBuildsByItemName(String itemName);
//    void disassociateChampion(Champions champion);
//    void disassociateItems(List<Items> items);
    Optional<Builds> getBuildForChampion(String champName);

}
