package org.church.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A PeriodContributionType.
 */
@Entity
@Table(name = "period_contribution_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PeriodContributionType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code")
    private String code;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("periodContributionTypes")
    private PeriodType periodType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties({
        "periodContributionTypes",
        "members",
        "email",
        "phoneNumber",
        "address",
        "latitude",
        "longitude",
        "otherDetails",
        "parent",
        "type"
    })
    private Church church;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public PeriodContributionType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

    public PeriodContributionType periodType(PeriodType periodType) {
        this.periodType = periodType;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPeriodType(PeriodType periodType) {
        this.periodType = periodType;
    }

    public Church getChurch() {
        return church;
    }

    public PeriodContributionType church(Church church) {
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
        if (!(o instanceof PeriodContributionType)) {
            return false;
        }
        return id != null && id.equals(((PeriodContributionType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PeriodContributionType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
