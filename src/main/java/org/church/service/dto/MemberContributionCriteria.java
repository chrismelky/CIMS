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
 * Criteria class for the {@link org.church.domain.MemberContribution} entity. This class is used
 * in {@link org.church.web.rest.MemberContributionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /member-contributions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MemberContributionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter paymentDate;

    private BigDecimalFilter amount;

    private LongFilter memberId;

    private LongFilter churchId;

    private LongFilter paymentMethodId;

    private LongFilter promiseId;

    private LongFilter typeId;

    public MemberContributionCriteria(){
    }

    public MemberContributionCriteria(MemberContributionCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.paymentDate = other.paymentDate == null ? null : other.paymentDate.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.memberId = other.memberId == null ? null : other.memberId.copy();
        this.churchId = other.churchId == null ? null : other.churchId.copy();
        this.paymentMethodId = other.paymentMethodId == null ? null : other.paymentMethodId.copy();
        this.promiseId = other.promiseId == null ? null : other.promiseId.copy();
        this.typeId = other.typeId == null ? null : other.typeId.copy();
    }

    @Override
    public MemberContributionCriteria copy() {
        return new MemberContributionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateFilter paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public LongFilter getMemberId() {
        return memberId;
    }

    public void setMemberId(LongFilter memberId) {
        this.memberId = memberId;
    }

    public LongFilter getChurchId() {
        return churchId;
    }

    public void setChurchId(LongFilter churchId) {
        this.churchId = churchId;
    }

    public LongFilter getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(LongFilter paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public LongFilter getPromiseId() {
        return promiseId;
    }

    public void setPromiseId(LongFilter promiseId) {
        this.promiseId = promiseId;
    }

    public LongFilter getTypeId() {
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MemberContributionCriteria that = (MemberContributionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(paymentDate, that.paymentDate) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(memberId, that.memberId) &&
            Objects.equals(churchId, that.churchId) &&
            Objects.equals(paymentMethodId, that.paymentMethodId) &&
            Objects.equals(promiseId, that.promiseId) &&
            Objects.equals(typeId, that.typeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        paymentDate,
        amount,
        memberId,
        churchId,
        paymentMethodId,
        promiseId,
        typeId
        );
    }

    @Override
    public String toString() {
        return "MemberContributionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (paymentDate != null ? "paymentDate=" + paymentDate + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (memberId != null ? "memberId=" + memberId + ", " : "") +
                (churchId != null ? "churchId=" + churchId + ", " : "") +
                (paymentMethodId != null ? "paymentMethodId=" + paymentMethodId + ", " : "") +
                (promiseId != null ? "promiseId=" + promiseId + ", " : "") +
                (typeId != null ? "typeId=" + typeId + ", " : "") +
            "}";
    }

}
