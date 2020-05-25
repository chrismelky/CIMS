package org.church.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link org.church.domain.PeriodContribution} entity. This class is used
 * in {@link org.church.web.rest.PeriodContributionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /period-contributions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PeriodContributionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter amountPromised;

    private BigDecimalFilter amountContributed;

    private StringFilter description;

    private LocalDateFilter dueDate;

    private LongFilter periodId;

    private LongFilter memberPromiseId;

    public PeriodContributionCriteria() {
    }

    public PeriodContributionCriteria(PeriodContributionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.amountPromised = other.amountPromised == null ? null : other.amountPromised.copy();
        this.amountContributed = other.amountContributed == null ? null : other.amountContributed.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.dueDate = other.dueDate == null ? null : other.dueDate.copy();
        this.periodId = other.periodId == null ? null : other.periodId.copy();
        this.memberPromiseId = other.memberPromiseId == null ? null : other.memberPromiseId.copy();
    }

    @Override
    public PeriodContributionCriteria copy() {
        return new PeriodContributionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BigDecimalFilter getAmountPromised() {
        return amountPromised;
    }

    public void setAmountPromised(BigDecimalFilter amountPromised) {
        this.amountPromised = amountPromised;
    }

    public BigDecimalFilter getAmountContributed() {
        return amountContributed;
    }

    public void setAmountContributed(BigDecimalFilter amountContributed) {
        this.amountContributed = amountContributed;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LocalDateFilter getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateFilter dueDate) {
        this.dueDate = dueDate;
    }

    public LongFilter getPeriodId() {
        return periodId;
    }

    public void setPeriodId(LongFilter periodId) {
        this.periodId = periodId;
    }

    public LongFilter getMemberPromiseId() {
        return memberPromiseId;
    }

    public void setMemberPromiseId(LongFilter memberPromiseId) {
        this.memberPromiseId = memberPromiseId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PeriodContributionCriteria that = (PeriodContributionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(amountPromised, that.amountPromised) &&
            Objects.equals(amountContributed, that.amountContributed) &&
            Objects.equals(description, that.description) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(periodId, that.periodId) &&
            Objects.equals(memberPromiseId, that.memberPromiseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        amountPromised,
        amountContributed,
        description,
        dueDate,
        periodId,
        memberPromiseId
        );
    }

    @Override
    public String toString() {
        return "PeriodContributionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (amountPromised != null ? "amountPromised=" + amountPromised + ", " : "") +
                (amountContributed != null ? "amountContributed=" + amountContributed + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (dueDate != null ? "dueDate=" + dueDate + ", " : "") +
                (periodId != null ? "periodId=" + periodId + ", " : "") +
                (memberPromiseId != null ? "memberPromiseId=" + memberPromiseId + ", " : "") +
            "}";
    }

}
