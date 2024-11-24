package edu.setokk.astrocluster.repository;

import edu.setokk.astrocluster.model.PercentagePerClusterJpo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PercentagePerClusterRepository extends JpaRepository<PercentagePerClusterJpo, PercentagePerClusterJpo.PercentagePerClusterId> {
    @Query(value = "SELECT ppc FROM PercentagePerClusterJpo ppc WHERE ppc.id.analysisId =:analysisId")
    Set<PercentagePerClusterJpo> findAllByAnalysisId(@Param("analysisId") long analysisId);
}
