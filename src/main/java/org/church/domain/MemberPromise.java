package org.church.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A MemberPromise.
 */
@Entity
@Table(name = "member_promise")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MemberPromise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "promise_date", nullable = false)
    private LocalDate promiseDate;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Column(name = "other_promise")
    private String otherPromise;

    @Column(name = "fulfillment_date")
    private LocalDate fulfillmentDate;

    @Column(name = "is_fulfilled")
    private Boolean isFulfilled;

    @Column(name = "total_contribution", precision = 21, scale = 2)
    private BigDecimal totalContribution = BigDecimal.valueOf(0.0);

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("memberPromises")
    private Member member;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("memberPromises")
    private FinancialYear financialYear;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("memberPromises")
    private Church church;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("memberPromises")
    private PeriodContributionType periodContributionType;

    @OneToMany(mappedBy = "memberPromise", fetch = FetchType.EAGER)
    private List<PeriodContribution> periodContributions = new ArrayList<>();

    public MemberPromise() {}

    public MemberPromise(LocalDate promiseDate, PeriodContributionType periodContributionType, Church church, Member member, BigDecimal amount, FinancialYear fy) {
        this.promiseDate = promiseDate;
        this.periodContributionType = periodContributionType;
        this.church = church;
        this.member = member;
        this.amount = amount;
        this.financialYear = fy;
    }

    public List<PeriodContribution> getPeriodContributions() {
        return periodContributions;
    }

    public void setPeriodContributions(List<PeriodContribution> periodContributions) {
        this.periodContributions = periodContributions;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPromiseDate() {
        return promiseDate;
    }

    public void setPromiseDate(LocalDate promiseDate) {
        this.promiseDate = promiseDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOtherPromise() {
        return otherPromise;
    }

    public void setOtherPromise(String otherPromise) {
        this.otherPromise = otherPromise;
    }

    public LocalDate getFulfillmentDate() {
        return fulfillmentDate;
    }

    public void setFulfillmentDate(LocalDate fulfillmentDate) {
        this.fulfillmentDate = fulfillmentDate;
    }

    public Boolean isIsFulfilled() {
        return isFulfilled;
    }

    public void setIsFulfilled(Boolean isFulfilled) {
        this.isFulfilled = isFulfilled;
    }

    public BigDecimal getTotalContribution() {
        return totalContribution;
    }

    public void setTotalContribution(BigDecimal totalContribution) {
        this.totalContribution = totalContribution;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public FinancialYear getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(FinancialYear financialYear) {
        this.financialYear = financialYear;
    }

    public Church getChurch() {
        return church;
    }

    public void setChurch(Church church) {
        this.church = church;
    }

    public PeriodContributionType getPeriodContributionType() {
        return periodContributionType;
    }

    public void setPeriodContributionType(PeriodContributionType periodContributionType) {
        this.periodContributionType = periodContributionType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberPromise)) {
            return false;
        }
        return id != null && id.equals(((MemberPromise) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MemberPromise{" +
            "id=" + getId() +
            ", promiseDate='" + getPromiseDate() + "'" +
            ", amount=" + getAmount() +
            ", otherPromise='" + getOtherPromise() + "'" +
            ", fulfillmentDate='" + getFulfillmentDate() + "'" +
            ", isFulfilled='" + isIsFulfilled() + "'" +
            ", totalContribution=" + getTotalContribution() +
            "}";
    }
}
