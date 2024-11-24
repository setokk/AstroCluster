package edu.setokk.astrocluster.service;

import edu.setokk.astrocluster.core.mapper.PercentagePerClusterMapper;
import edu.setokk.astrocluster.model.PercentagePerClusterJpo;
import edu.setokk.astrocluster.model.dto.PercentagePerClusterDto;
import edu.setokk.astrocluster.repository.PercentagePerClusterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class PercentagePerClusterService {
    private final PercentagePerClusterRepository percentagePerClusterRepository;

    @Autowired
    public PercentagePerClusterService(PercentagePerClusterRepository percentagePerClusterRepository) {
        this.percentagePerClusterRepository = percentagePerClusterRepository;
    }

    public List<PercentagePerClusterDto> findAllByAnalysisId(long analysisId) {
        Set<PercentagePerClusterJpo> percentagePerClusters = percentagePerClusterRepository.findAllByAnalysisId(analysisId);
        return PercentagePerClusterMapper.INSTANCE.mapPercentagePerClusters(percentagePerClusters);
    }
}
