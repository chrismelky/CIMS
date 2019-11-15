package org.church.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import org.church.domain.enumeration.RelativeType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link org.church.domain.MemberRelative} entity. This class is used
 * in {@link org.church.web.rest.MemberRelativeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /member-relatives?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MemberRelativeCriteria implements Serializable, Criteria {
    /**
     * Class for filtering RelativeType
     */
    public static class RelativeTypeFilter extends Filter<RelativeType> {

        public RelativeTypeFilter() {
        }

        public RelativeTypeFilter(RelativeTypeFilter filter) {
            super(filter);
        }

        @Override
        public RelativeTypeFilter copy() {
            return new RelativeTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private RelativeTypeFilter relativeType;

    private StringFilter relativeFullName;

    private StringFilter relativePhoneNumber;

    private LongFilter memberId;

    public MemberRelativeCriteria(){
    }

    public MemberRelativeCriteria(MemberRelativeCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.relativeType = other.relativeType == null ? null : other.relativeType.copy();
        this.relativeFullName = other.relativeFullName == null ? null : other.relativeFullName.copy();
        this.relativePhoneNumber = other.relativePhoneNumber == null ? null : other.relativePhoneNumber.copy();
        this.memberId = other.memberId == null ? null : other.memberId.copy();
    }

    @Override
    public MemberRelativeCriteria copy() {
        return new MemberRelativeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public RelativeTypeFilter getRelativeType() {
        return relativeType;
    }

    public void setRelativeType(RelativeTypeFilter relativeType) {
        this.relativeType = relativeType;
    }

    public StringFilter getRelativeFullName() {
        return relativeFullName;
    }

    public void setRelativeFullName(StringFilter relativeFullName) {
        this.relativeFullName = relativeFullName;
    }

    public StringFilter getRelativePhoneNumber() {
        return relativePhoneNumber;
    }

    public void setRelativePhoneNumber(StringFilter relativePhoneNumber) {
        this.relativePhoneNumber = relativePhoneNumber;
    }

    public LongFilter getMemberId() {
        return memberId;
    }

    public void setMemberId(LongFilter memberId) {
        this.memberId = memberId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MemberRelativeCriteria that = (MemberRelativeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(relativeType, that.relativeType) &&
            Objects.equals(relativeFullName, that.relativeFullName) &&
            Objects.equals(relativePhoneNumber, that.relativePhoneNumber) &&
            Objects.equals(memberId, that.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        relativeType,
        relativeFullName,
        relativePhoneNumber,
        memberId
        );
    }

    @Override
    public String toString() {
        return "MemberRelativeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (relativeType != null ? "relativeType=" + relativeType + ", " : "") +
                (relativeFullName != null ? "relativeFullName=" + relativeFullName + ", " : "") +
                (relativePhoneNumber != null ? "relativePhoneNumber=" + relativePhoneNumber + ", " : "") +
                (memberId != null ? "memberId=" + memberId + ", " : "") +
            "}";
    }

}
