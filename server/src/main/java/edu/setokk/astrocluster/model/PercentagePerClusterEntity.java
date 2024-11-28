package edu.setokk.astrocluster.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "percentage_per_cluster")
public class PercentagePerClusterEntity {
    @EmbeddedId
    private PercentagePerClusterId id;

    @Column(name = "percentage_in_project", updatable = false, nullable = false)
    private Double percentageInProject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private AnalysisEntity analysisEntity;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Embeddable
    public static class PercentagePerClusterId {
        @Column(name = "analysis_id")
        private Long analysisId;
        @Column(name = "cluster_label")
        private Integer clusterLabel;
    }
}