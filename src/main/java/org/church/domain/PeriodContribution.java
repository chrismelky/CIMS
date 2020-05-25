package org.church.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A PeriodContribution.
 */
@Entity
@Table(name = "period_contribution")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PeriodContribution implements Serializable {

    private static final long serialVersionUID = 1L;

    public PeriodContribution() {}

    public PeriodContribution(BigDecimal amount, LocalDate endDate, Period p, MemberPromise promise) {
        this.amountPromised = amount;
        this.dueDate = endDate;
        this.period = p;
        this.memberPromise = promise;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "amount_promised", precision = 21, scale = 2, nullable = false)
    private BigDecimal amountPromised;

    @Column(name = "amount_contributed", precision = 21, scale = 2)
    private BigDecimal amountContributed = BigDecimal.valueOf(0);

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("periodContributions")
    private Period period;

    @ManyToOne
    @JsonIgnoreProperties("periodContributions")
    @JoinColumn(name = "member_promise_id")
    private MemberPromise memberPromise;


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmountPromised() {
        return amountPromised;
    }

    public PeriodContribution amountPromised(BigDecimal amountPromised) {
        this.amountPromised = amountPromised;
        return this;
    }

    public void setAmountPromised(BigDecimal amountPromised) {
        this.amountPromised = amountPromised;
    }

    public BigDecimal getAmountContributed() {
        return amountContributed;
    }

    public PeriodContribution amountContributed(BigDecimal amountContributed) {
        this.amountContributed = amountContributed;
        return this;
    }

    public void setAmountContributed(BigDecimal amountContributed) {
        this.amountContributed = amountContributed;
    }

    public String getDescription() {
        return description;
    }

    public PeriodContribution description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public PeriodContribution dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Period getPeriod() {
        return period;
    }

    public PeriodContribution period(Period period) {
        this.period = period;
        return this;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public MemberPromise getMemberPromise() {
        return memberPromise;
    }

    public PeriodContribution memberPromise(MemberPromise memberPromise) {
        this.memberPromise = memberPromise;
        return this;
    }

    public void setMemberPromise(MemberPromise memberPromise) {
        this.memberPromise = memberPromise;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PeriodContribution)) {
            return false;
        }
        return id != null && id.equals(((PeriodContribution) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PeriodContribution{" +
            "id=" + getId() +
            ", amountPromised=" + getAmountPromised() +
            ", amountContributed=" + getAmountContributed() +
            ", description='" + getDescription() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            "}";
    }
}
