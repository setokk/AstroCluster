package edu.setokk.astrocluster.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "user_ac",
        indexes = {
                @Index(name = "user_ac_email_index", columnList = "email"),
                @Index(name = "user_ac_username_index", columnList = "username")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "user_ac_email_unique", columnNames = "email"),
                @UniqueConstraint(name = "user_ac_username_unique", columnNames = "username")
        }
)
public class UserJpo {
    @Id
    @SequenceGenerator(
            name = "userAcSeqGen",
            sequenceName = "user_ac_seq",
            initialValue = 0,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "userAcSeqGen"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "username", updatable = false, nullable = false)
    private String username;

    @Column(name = "email", updatable = false, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(mappedBy = "user")
    private AnalysisJpo analysis;

    public UserJpo(long id) {
        this.id = id;
    }
}
