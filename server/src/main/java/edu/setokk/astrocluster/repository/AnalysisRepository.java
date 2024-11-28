package edu.setokk.astrocluster.repository;

import edu.setokk.astrocluster.model.AnalysisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnalysisRepository extends JpaRepository<AnalysisEntity, Long> {
    @Query(value = "SELECT a FROM AnalysisEntity a WHERE a.id=:analysisId AND a.user.id=:userId")
    Optional<AnalysisEntity> findByIdAndUserId(@Param("analysisId") long analysisId, @Param("userId") long userId);
}
