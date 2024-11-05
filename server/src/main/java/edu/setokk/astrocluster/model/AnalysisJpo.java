package edu.setokk.astrocluster.model;

import edu.setokk.astrocluster.util.StringUtils;
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
            allocationSize = 2
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "analysisSeqGen"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "project_uuid", updatable = false, nullable = false)
    private String projectUUID;

    @Column(name = "git_url", updatable = false, nullable = false)
    private String gitUrl;

    @OneToMany(mappedBy = "analysis", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ClusteringResultJpo> clusteringResults;

    public String getGitProjectName() {
        String splitGitUrl = StringUtils.splitByAndGetLast(gitUrl, "\\/");
        return StringUtils.splitByAndGetFirst(splitGitUrl, "\\."); // Remove .git
    }
}
