package edu.setokk.astrocluster.analysis;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(
        name = "analysis",
        indexes = {
          @Index(name = "analysis_project_uuid_index", columnList = "project_uuid")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "analysis_project_uuid_unique", columnNames = "project_uuid")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisJpo {

    @Id
    @SequenceGenerator(
            name = "analysisSeqGen",
            sequenceName = "analysis_seq",
            allocationSize = 10
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "analysisSeqGen"
    )
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "project_uuid", updatable = false)
    private String projectUUID;

    @OneToMany(mappedBy = "analysis", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ClusteringResultJpo> clusteringResults;
}
