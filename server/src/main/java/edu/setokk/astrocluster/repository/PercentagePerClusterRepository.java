package edu.setokk.astrocluster.repository;

import edu.setokk.astrocluster.model.PercentagePerClusterJpo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PercentagePerClusterRepository extends JpaRepository<PercentagePerClusterJpo, PercentagePerClusterJpo.PercentagePerClusterId> {
    @Query(value = "SELECT ppc FROM percentage_per_cluster WHERE analysis_id=:analysisId", nativeQuery = true)
    Set<PercentagePerClusterJpo> findAllByAnalysisId(@Param("analysisId") long analysisId);
}
