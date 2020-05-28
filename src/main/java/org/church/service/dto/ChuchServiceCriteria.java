package org.church.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import org.church.domain.enumeration.WeekDay;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link org.church.domain.ChuchService} entity. This class is used
 * in {@link org.church.web.rest.ChuchServiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /chuch-services?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ChuchServiceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter day;

    private StringFilter startTime;

    private StringFilter endTime;

    private LongFilter churchId;

    public ChuchServiceCriteria(){
    }

    public ChuchServiceCriteria(ChuchServiceCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.day = other.day == null ? null : other.day.copy();
        this.startTime = other.startTime == null ? null : other.startTime.copy();
        this.endTime = other.endTime == null ? null : other.endTime.copy();
        this.churchId = other.churchId == null ? null : other.churchId.copy();
    }

    @Override
    public ChuchServiceCriteria copy() {
        return new ChuchServiceCriteria(this);
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

    public StringFilter getDay() {
        return day;
    }

    public void setDay(StringFilter day) {
        this.day = day;
    }

    public StringFilter getStartTime() {
        return startTime;
    }

    public void setStartTime(StringFilter startTime) {
        this.startTime = startTime;
    }

    public StringFilter getEndTime() {
        return endTime;
    }

    public void setEndTime(StringFilter endTime) {
        this.endTime = endTime;
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
        final ChuchServiceCriteria that = (ChuchServiceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(day, that.day) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(churchId, that.churchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        day,
        startTime,
        endTime,
        churchId
        );
    }

    @Override
    public String toString() {
        return "ChuchServiceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (day != null ? "day=" + day + ", " : "") +
                (startTime != null ? "startTime=" + startTime + ", " : "") +
                (endTime != null ? "endTime=" + endTime + ", " : "") +
                (churchId != null ? "churchId=" + churchId + ", " : "") +
            "}";
    }

}
