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
 * Criteria class for the {@link org.church.domain.PeriodContributionItem} entity. This class is used
 * in {@link org.church.web.rest.PeriodContributionItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /period-contribution-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PeriodContributionItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter amount;

    private StringFilter description;

    private LocalDateFilter dateReceived;

    private StringFilter receivedBy;

    private LongFilter periodContributionId;

    public PeriodContributionItemCriteria(){
    }

    public PeriodContributionItemCriteria(PeriodContributionItemCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.dateReceived = other.dateReceived == null ? null : other.dateReceived.copy();
        this.receivedBy = other.receivedBy == null ? null : other.receivedBy.copy();
        this.periodContributionId = other.periodContributionId == null ? null : other.periodContributionId.copy();
    }

    @Override
    public PeriodContributionItemCriteria copy() {
        return new PeriodContributionItemCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LocalDateFilter getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(LocalDateFilter dateReceived) {
        this.dateReceived = dateReceived;
    }

    public StringFilter getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(StringFilter receivedBy) {
        this.receivedBy = receivedBy;
    }

    public LongFilter getPeriodContributionId() {
        return periodContributionId;
    }

    public void setPeriodContributionId(LongFilter periodContributionId) {
        this.periodContributionId = periodContributionId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PeriodContributionItemCriteria that = (PeriodContributionItemCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(description, that.description) &&
            Objects.equals(dateReceived, that.dateReceived) &&
            Objects.equals(receivedBy, that.receivedBy) &&
            Objects.equals(periodContributionId, that.periodContributionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        amount,
        description,
        dateReceived,
        receivedBy,
        periodContributionId
        );
    }

    @Override
    public String toString() {
        return "PeriodContributionItemCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (dateReceived != null ? "dateReceived=" + dateReceived + ", " : "") +
                (receivedBy != null ? "receivedBy=" + receivedBy + ", " : "") +
                (periodContributionId != null ? "periodContributionId=" + periodContributionId + ", " : "") +
            "}";
    }

}
