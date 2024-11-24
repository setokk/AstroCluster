package edu.setokk.astrocluster.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
public class AnalysisJpo {

    @Id
    @SequenceGenerator(
            name = "analysisSeqGen",
            sequenceName = "analysis_seq",
            allocationSize = 1
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

    @Column(name = "project_lang", updatable = false, nullable = false)
    private String projectLang;

    @Column(name = "cluster_results")
    @OneToMany(mappedBy = "analysis", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ClusterResultJpo> clusterResults;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private UserJpo user;

    public AnalysisJpo(Long id) {
        this.id = id;
    }

    public AnalysisJpo(Long id, String projectUUID, String gitUrl, String projectLang, UserJpo user) {
        this.id = id;
        this.projectUUID = projectUUID;
        this.gitUrl = gitUrl;
        this.projectLang = projectLang;
        this.user = user;
    }
}
