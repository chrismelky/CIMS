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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link org.church.domain.Period} entity. This class is used
 * in {@link org.church.web.rest.PeriodResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /periods?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PeriodCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private BooleanFilter isCurrent;

    private LongFilter typeId;

    private LongFilter financialYearId;

    public PeriodCriteria() {
    }

    public PeriodCriteria(PeriodCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.isCurrent = other.isCurrent == null ? null : other.isCurrent.copy();
        this.typeId = other.typeId == null ? null : other.typeId.copy();
        this.financialYearId = other.financialYearId == null ? null : other.financialYearId.copy();
    }

    @Override
    public PeriodCriteria copy() {
        return new PeriodCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public BooleanFilter getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(BooleanFilter isCurrent) {
        this.isCurrent = isCurrent;
    }

    public LongFilter getTypeId() {
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
    }

    public LongFilter getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(LongFilter financialYearId) {
        this.financialYearId = financialYearId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PeriodCriteria that = (PeriodCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(isCurrent, that.isCurrent) &&
            Objects.equals(typeId, that.typeId) &&
            Objects.equals(financialYearId, that.financialYearId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        startDate,
        endDate,
        isCurrent,
        typeId,
        financialYearId
        );
    }

    @Override
    public String toString() {
        return "PeriodCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (startDate != null ? "startDate=" + startDate + ", " : "") +
                (endDate != null ? "endDate=" + endDate + ", " : "") +
                (isCurrent != null ? "isCurrent=" + isCurrent + ", " : "") +
                (typeId != null ? "typeId=" + typeId + ", " : "") +
                (financialYearId != null ? "financialYearId=" + financialYearId + ", " : "") +
            "}";
    }

}
