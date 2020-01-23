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

/**
 * Criteria class for the {@link org.church.domain.PeriodContributionType} entity. This class is used
 * in {@link org.church.web.rest.PeriodContributionTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /period-contribution-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PeriodContributionTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter periodTypeId;

    private LongFilter churchId;

    public PeriodContributionTypeCriteria(){
    }

    public PeriodContributionTypeCriteria(PeriodContributionTypeCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.periodTypeId = other.periodTypeId == null ? null : other.periodTypeId.copy();
        this.churchId = other.churchId == null ? null : other.churchId.copy();
    }

    @Override
    public PeriodContributionTypeCriteria copy() {
        return new PeriodContributionTypeCriteria(this);
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

    public LongFilter getPeriodTypeId() {
        return periodTypeId;
    }

    public void setPeriodTypeId(LongFilter periodTypeId) {
        this.periodTypeId = periodTypeId;
    }

    public LongFilter getChurchId() {
        return churchId;
    }

    public void setChurchId(LongFilter churchId) {
        this.churchId = churchId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PeriodContributionTypeCriteria that = (PeriodContributionTypeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(periodTypeId, that.periodTypeId) &&
            Objects.equals(churchId, that.churchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        periodTypeId,
        churchId
        );
    }

    @Override
    public String toString() {
        return "PeriodContributionTypeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (periodTypeId != null ? "periodTypeId=" + periodTypeId + ", " : "") +
                (churchId != null ? "churchId=" + churchId + ", " : "") +
            "}";
    }

}
