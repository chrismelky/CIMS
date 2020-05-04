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
 * Criteria class for the {@link org.church.domain.MemberPromise} entity. This class is used
 * in {@link org.church.web.rest.MemberPromiseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /member-promises?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MemberPromiseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter promiseDate;

    private BigDecimalFilter amount;

    private StringFilter otherPromise;

    private LocalDateFilter fulfillmentDate;

    private BooleanFilter isFulfilled;

    private BigDecimalFilter totatlContribution;

    private LongFilter memberId;

    private LongFilter churchActivityId;

    private LongFilter financialYearId;

    private LongFilter churchId;

    private LongFilter periodContributionTypeId;

    public MemberPromiseCriteria() {
    }

    public MemberPromiseCriteria(MemberPromiseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.promiseDate = other.promiseDate == null ? null : other.promiseDate.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.otherPromise = other.otherPromise == null ? null : other.otherPromise.copy();
        this.fulfillmentDate = other.fulfillmentDate == null ? null : other.fulfillmentDate.copy();
        this.isFulfilled = other.isFulfilled == null ? null : other.isFulfilled.copy();
        this.totatlContribution = other.totatlContribution == null ? null : other.totatlContribution.copy();
        this.memberId = other.memberId == null ? null : other.memberId.copy();
        this.churchActivityId = other.churchActivityId == null ? null : other.churchActivityId.copy();
        this.financialYearId = other.financialYearId == null ? null : other.financialYearId.copy();
        this.churchId = other.churchId == null ? null : other.churchId.copy();
        this.periodContributionTypeId = other.periodContributionTypeId == null ? null : other.periodContributionTypeId.copy();
    }

    @Override
    public MemberPromiseCriteria copy() {
        return new MemberPromiseCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getPromiseDate() {
        return promiseDate;
    }

    public void setPromiseDate(LocalDateFilter promiseDate) {
        this.promiseDate = promiseDate;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public StringFilter getOtherPromise() {
        return otherPromise;
    }

    public void setOtherPromise(StringFilter otherPromise) {
        this.otherPromise = otherPromise;
    }

    public LocalDateFilter getFulfillmentDate() {
        return fulfillmentDate;
    }

    public void setFulfillmentDate(LocalDateFilter fulfillmentDate) {
        this.fulfillmentDate = fulfillmentDate;
    }

    public BooleanFilter getIsFulfilled() {
        return isFulfilled;
    }

    public void setIsFulfilled(BooleanFilter isFulfilled) {
        this.isFulfilled = isFulfilled;
    }

    public BigDecimalFilter getTotatlContribution() {
        return totatlContribution;
    }

    public void setTotatlContribution(BigDecimalFilter totatlContribution) {
        this.totatlContribution = totatlContribution;
    }

    public LongFilter getMemberId() {
        return memberId;
    }

    public void setMemberId(LongFilter memberId) {
        this.memberId = memberId;
    }

    public LongFilter getChurchActivityId() {
        return churchActivityId;
    }

    public void setChurchActivityId(LongFilter churchActivityId) {
        this.churchActivityId = churchActivityId;
    }

    public LongFilter getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(LongFilter financialYearId) {
        this.financialYearId = financialYearId;
    }

    public LongFilter getChurchId() {
        return churchId;
    }

    public void setChurchId(LongFilter churchId) {
        this.churchId = churchId;
    }

    public LongFilter getPeriodContributionTypeId() {
        return periodContributionTypeId;
    }

    public void setPeriodContributionTypeId(LongFilter periodContributionTypeId) {
        this.periodContributionTypeId = periodContributionTypeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MemberPromiseCriteria that = (MemberPromiseCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(promiseDate, that.promiseDate) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(otherPromise, that.otherPromise) &&
            Objects.equals(fulfillmentDate, that.fulfillmentDate) &&
            Objects.equals(isFulfilled, that.isFulfilled) &&
            Objects.equals(totatlContribution, that.totatlContribution) &&
            Objects.equals(memberId, that.memberId) &&
            Objects.equals(churchActivityId, that.churchActivityId) &&
            Objects.equals(financialYearId, that.financialYearId) &&
            Objects.equals(churchId, that.churchId) &&
            Objects.equals(periodContributionTypeId, that.periodContributionTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        promiseDate,
        amount,
        otherPromise,
        fulfillmentDate,
        isFulfilled,
        totatlContribution,
        memberId,
        churchActivityId,
        financialYearId,
        churchId,
        periodContributionTypeId
        );
    }

    @Override
    public String toString() {
        return "MemberPromiseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (promiseDate != null ? "promiseDate=" + promiseDate + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (otherPromise != null ? "otherPromise=" + otherPromise + ", " : "") +
                (fulfillmentDate != null ? "fulfillmentDate=" + fulfillmentDate + ", " : "") +
                (isFulfilled != null ? "isFulfilled=" + isFulfilled + ", " : "") +
                (totatlContribution != null ? "totatlContribution=" + totatlContribution + ", " : "") +
                (memberId != null ? "memberId=" + memberId + ", " : "") +
                (churchActivityId != null ? "churchActivityId=" + churchActivityId + ", " : "") +
                (financialYearId != null ? "financialYearId=" + financialYearId + ", " : "") +
                (churchId != null ? "churchId=" + churchId + ", " : "") +
                (periodContributionTypeId != null ? "periodContributionTypeId=" + periodContributionTypeId + ", " : "") +
            "}";
    }

}
