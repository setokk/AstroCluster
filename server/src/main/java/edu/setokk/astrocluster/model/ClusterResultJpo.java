package edu.setokk.astrocluster.model;

import edu.setokk.astrocluster.util.StringUtils;
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
public class ClusterResultJpo {

    @Id
    @SequenceGenerator(
            name = "clusteringResultSeqGen",
            sequenceName = "clustering_result_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "clusteringResultSeqGen"
    )
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "filepath", nullable = false)
    private String filepath;

    @Column(name = "cluster_label", nullable = false)
    private String clusterLabel;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_id", nullable = false)
    private AnalysisJpo analysis;

    public String getFileName() {
        return StringUtils.splitByAndGetLast(filepath, "[\\\\/]");
    }
}
