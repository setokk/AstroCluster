package edu.setokk.astrocluster.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "cluster_result",
        uniqueConstraints = {
                @UniqueConstraint(name = "cluster_result_file_path_unique", columnNames = "filepath")
        }
)
public class ClusterResultJpo {

    @Id
    @SequenceGenerator(
            name = "clusterResultSeqGen",
            sequenceName = "cluster_result_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "clusterResultSeqGen"
    )
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "filepath", nullable = false)
    private String filepath;

    @Column(name = "cluster_label", nullable = false)
    private Integer clusterLabel;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_id", nullable = false)
    private AnalysisJpo analysis;
}
