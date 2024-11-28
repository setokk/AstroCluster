package edu.setokk.astrocluster.repository;

import edu.setokk.astrocluster.model.PercentagePerClusterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PercentagePerClusterRepository extends JpaRepository<PercentagePerClusterEntity, PercentagePerClusterEntity.PercentagePerClusterId> {
    @Query(value = "SELECT ppc FROM PercentagePerClusterEntity ppc WHERE ppc.id.analysisId =:analysisId")
    Set<PercentagePerClusterEntity> findAllByAnalysisId(@Param("analysisId") long analysisId);
}
