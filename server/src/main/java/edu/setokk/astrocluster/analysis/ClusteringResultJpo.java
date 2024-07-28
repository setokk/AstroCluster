package edu.setokk.astrocluster.analysis;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "clustering_result",
        uniqueConstraints = {
                @UniqueConstraint(name = "clustering_result_file_path_unique", columnNames = "file_path")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClusteringResultJpo {

    @Id
    @SequenceGenerator(
            name = "clusteringResultSeqGen",
            sequenceName = "clustering_result_seq",
            allocationSize = 10
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "clusteringResultSeqGen"
    )
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "similarity_score", nullable = false)
    private String similarityScore;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_id", nullable = false)
    private AnalysisJpo analysis;
}
