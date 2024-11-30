package edu.setokk.astrocluster.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

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
public class UserEntity {
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

    @OneToMany(mappedBy = "user")
    private Set<AnalysisEntity> analyses;

    public UserEntity(long id) {
        this.id = id;
    }

    public UserEntity(long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserEntity(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
