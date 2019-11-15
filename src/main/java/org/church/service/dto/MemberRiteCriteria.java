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
 * Criteria class for the {@link org.church.domain.MemberRite} entity. This class is used
 * in {@link org.church.web.rest.MemberRiteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /member-rites?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MemberRiteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dateReceived;

    private LongFilter riteId;

    private LongFilter memberId;

    private LongFilter churchId;

    public MemberRiteCriteria(){
    }

    public MemberRiteCriteria(MemberRiteCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.dateReceived = other.dateReceived == null ? null : other.dateReceived.copy();
        this.riteId = other.riteId == null ? null : other.riteId.copy();
        this.memberId = other.memberId == null ? null : other.memberId.copy();
        this.churchId = other.churchId == null ? null : other.churchId.copy();
    }

    @Override
    public MemberRiteCriteria copy() {
        return new MemberRiteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(LocalDateFilter dateReceived) {
        this.dateReceived = dateReceived;
    }

    public LongFilter getRiteId() {
        return riteId;
    }

    public void setRiteId(LongFilter riteId) {
        this.riteId = riteId;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MemberRiteCriteria that = (MemberRiteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(dateReceived, that.dateReceived) &&
            Objects.equals(riteId, that.riteId) &&
            Objects.equals(memberId, that.memberId) &&
            Objects.equals(churchId, that.churchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        dateReceived,
        riteId,
        memberId,
        churchId
        );
    }

    @Override
    public String toString() {
        return "MemberRiteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (dateReceived != null ? "dateReceived=" + dateReceived + ", " : "") +
                (riteId != null ? "riteId=" + riteId + ", " : "") +
                (memberId != null ? "memberId=" + memberId + ", " : "") +
                (churchId != null ? "churchId=" + churchId + ", " : "") +
            "}";
    }

}
