package org.church.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @NotNull
    @Column(name = "date_received", nullable = false)
    private LocalDate dateReceived;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("memberRites")
    private Rite rite;

    @ManyToOne
    @JsonIgnoreProperties("memberRites")
    private Member member;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("memberRites")
    private Church church;

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

    public Rite getRite() {
        return rite;
    }

    public MemberRite rite(Rite rite) {
        this.rite = rite;
        return this;
    }

    public void setRite(Rite rite) {
        this.rite = rite;
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

    public Church getChurch() {
        return church;
    }

    public MemberRite church(Church church) {
        this.church = church;
        return this;
    }

    public void setChurch(Church church) {
        this.church = church;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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
