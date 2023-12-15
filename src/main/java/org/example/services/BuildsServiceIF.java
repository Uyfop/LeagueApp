package org.example.services;

import org.example.tables.Builds;
import org.example.tables.Items;

import java.util.List;
import java.util.Optional;

public interface BuildsServiceIF {

    Optional<Builds> GetBuildById(Long id);
    List<Builds> ListAllBuilds();
    Builds saveBuild(String champname, List<String> itemnames, Builds build);
    boolean deleteBuildByID(Long id);

    boolean getBuildsByChampion(String champName);

    Optional<Builds> findBuildByChampion(String champName);
}
