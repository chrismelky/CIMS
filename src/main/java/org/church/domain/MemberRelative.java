package org.church.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

import org.church.domain.enumeration.RelativeType;

/**
 * A MemberRelative.
 */
@Entity
@Table(name = "member_relative")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MemberRelative implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "relative_type")
    private RelativeType relativeType;

    @Column(name = "relative_full_name")
    private String relativeFullName;

    @Column(name = "relative_phone_number")
    private String relativePhoneNumber;

    @ManyToOne
    @JsonIgnoreProperties("relatives")
    private Member member;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RelativeType getRelativeType() {
        return relativeType;
    }

    public MemberRelative relativeType(RelativeType relativeType) {
        this.relativeType = relativeType;
        return this;
    }

    public void setRelativeType(RelativeType relativeType) {
        this.relativeType = relativeType;
    }

    public String getRelativeFullName() {
        return relativeFullName;
    }

    public MemberRelative relativeFullName(String relativeFullName) {
        this.relativeFullName = relativeFullName;
        return this;
    }

    public void setRelativeFullName(String relativeFullName) {
        this.relativeFullName = relativeFullName;
    }

    public String getRelativePhoneNumber() {
        return relativePhoneNumber;
    }

    public MemberRelative relativePhoneNumber(String relativePhoneNumber) {
        this.relativePhoneNumber = relativePhoneNumber;
        return this;
    }

    public void setRelativePhoneNumber(String relativePhoneNumber) {
        this.relativePhoneNumber = relativePhoneNumber;
    }

    public Member getMember() {
        return member;
    }

    public MemberRelative member(Member member) {
        this.member = member;
        return this;
    }

    public void setMember(Member member) {
        this.member = member;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberRelative)) {
            return false;
        }
        return id != null && id.equals(((MemberRelative) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MemberRelative{" +
            "id=" + getId() +
            ", relativeType='" + getRelativeType() + "'" +
            ", relativeFullName='" + getRelativeFullName() + "'" +
            ", relativePhoneNumber='" + getRelativePhoneNumber() + "'" +
            "}";
    }
}
