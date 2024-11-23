package edu.setokk.astrocluster.repository;

import edu.setokk.astrocluster.model.AnalysisJpo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnalysisRepository extends JpaRepository<AnalysisJpo, Long> {
    @Query(value = "SELECT a FROM analysis WHERE id=:analysisId AND user_id=:userId", nativeQuery = true)
    Optional<AnalysisJpo> findByIdAndUserId(@Param("analysisId") long analysisId, @Param("analysisId") long userId);
}
