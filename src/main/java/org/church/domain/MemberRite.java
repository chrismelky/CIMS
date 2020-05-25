package org.church.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.church.domain.enumeration.RiteType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A MemberRite.
 */
@Entity
@Table(name = "member_rite")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MemberRite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date_received")
    private LocalDate dateReceived;

    @NotNull
    @Column(name = "rite_type", nullable = false)
    private RiteType riteType;

    @ManyToOne
    @JsonIgnoreProperties("memberRites")
    private Member member;

    public RiteType getRiteType() {
        return riteType;
    }

    public void setRiteType(RiteType riteType) {
        this.riteType = riteType;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateReceived() {
        return dateReceived;
    }

    public MemberRite dateReceived(LocalDate dateReceived) {
        this.dateReceived = dateReceived;
        return this;
    }

    public void setDateReceived(LocalDate dateReceived) {
        this.dateReceived = dateReceived;
    }

    public Member getMember() {
        return member;
    }

    public MemberRite member(Member member) {
        this.member = member;
        return this;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberRite)) {
            return false;
        }
        return id != null && id.equals(((MemberRite) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MemberRite{" +
            "id=" + getId() +
            ", dateReceived='" + getDateReceived() + "'" +
            "}";
    }
}
