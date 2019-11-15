package org.church.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import org.church.domain.enumeration.Gender;
import org.church.domain.enumeration.MaritalStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link org.church.domain.Member} entity. This class is used
 * in {@link org.church.web.rest.MemberResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /members?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MemberCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        public GenderFilter() {
        }

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }

    }
    /**
     * Class for filtering MaritalStatus
     */
    public static class MaritalStatusFilter extends Filter<MaritalStatus> {

        public MaritalStatusFilter() {
        }

        public MaritalStatusFilter(MaritalStatusFilter filter) {
            super(filter);
        }

        @Override
        public MaritalStatusFilter copy() {
            return new MaritalStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter middleName;

    private GenderFilter gender;

    private StringFilter phoneNumber;

    private StringFilter email;

    private LocalDateFilter dateOfBith;

    private StringFilter placeOfBith;

    private MaritalStatusFilter maritalStatus;

    private StringFilter work;

    private StringFilter placeOfWork;

    private BooleanFilter isActive;

    private BooleanFilter isDeceased;

    private LocalDateFilter deceasedDate;

    private LongFilter relativesId;

    private LongFilter churchId;

    private LongFilter churchCommunitiesId;

    private LongFilter memberRitesId;

    public MemberCriteria(){
    }

    public MemberCriteria(MemberCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.middleName = other.middleName == null ? null : other.middleName.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.dateOfBith = other.dateOfBith == null ? null : other.dateOfBith.copy();
        this.placeOfBith = other.placeOfBith == null ? null : other.placeOfBith.copy();
        this.maritalStatus = other.maritalStatus == null ? null : other.maritalStatus.copy();
        this.work = other.work == null ? null : other.work.copy();
        this.placeOfWork = other.placeOfWork == null ? null : other.placeOfWork.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.isDeceased = other.isDeceased == null ? null : other.isDeceased.copy();
        this.deceasedDate = other.deceasedDate == null ? null : other.deceasedDate.copy();
        this.relativesId = other.relativesId == null ? null : other.relativesId.copy();
        this.churchId = other.churchId == null ? null : other.churchId.copy();
        this.churchCommunitiesId = other.churchCommunitiesId == null ? null : other.churchCommunitiesId.copy();
        this.memberRitesId = other.memberRitesId == null ? null : other.memberRitesId.copy();
    }

    @Override
    public MemberCriteria copy() {
        return new MemberCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getMiddleName() {
        return middleName;
    }

    public void setMiddleName(StringFilter middleName) {
        this.middleName = middleName;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LocalDateFilter getDateOfBith() {
        return dateOfBith;
    }

    public void setDateOfBith(LocalDateFilter dateOfBith) {
        this.dateOfBith = dateOfBith;
    }

    public StringFilter getPlaceOfBith() {
        return placeOfBith;
    }

    public void setPlaceOfBith(StringFilter placeOfBith) {
        this.placeOfBith = placeOfBith;
    }

    public MaritalStatusFilter getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatusFilter maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public StringFilter getWork() {
        return work;
    }

    public void setWork(StringFilter work) {
        this.work = work;
    }

    public StringFilter getPlaceOfWork() {
        return placeOfWork;
    }

    public void setPlaceOfWork(StringFilter placeOfWork) {
        this.placeOfWork = placeOfWork;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public BooleanFilter getIsDeceased() {
        return isDeceased;
    }

    public void setIsDeceased(BooleanFilter isDeceased) {
        this.isDeceased = isDeceased;
    }

    public LocalDateFilter getDeceasedDate() {
        return deceasedDate;
    }

    public void setDeceasedDate(LocalDateFilter deceasedDate) {
        this.deceasedDate = deceasedDate;
    }

    public LongFilter getRelativesId() {
        return relativesId;
    }

    public void setRelativesId(LongFilter relativesId) {
        this.relativesId = relativesId;
    }

    public LongFilter getChurchId() {
        return churchId;
    }

    public void setChurchId(LongFilter churchId) {
        this.churchId = churchId;
    }

    public LongFilter getChurchCommunitiesId() {
        return churchCommunitiesId;
    }

    public void setChurchCommunitiesId(LongFilter churchCommunitiesId) {
        this.churchCommunitiesId = churchCommunitiesId;
    }

    public LongFilter getMemberRitesId() {
        return memberRitesId;
    }

    public void setMemberRitesId(LongFilter memberRitesId) {
        this.memberRitesId = memberRitesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MemberCriteria that = (MemberCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(middleName, that.middleName) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(email, that.email) &&
            Objects.equals(dateOfBith, that.dateOfBith) &&
            Objects.equals(placeOfBith, that.placeOfBith) &&
            Objects.equals(maritalStatus, that.maritalStatus) &&
            Objects.equals(work, that.work) &&
            Objects.equals(placeOfWork, that.placeOfWork) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(isDeceased, that.isDeceased) &&
            Objects.equals(deceasedDate, that.deceasedDate) &&
            Objects.equals(relativesId, that.relativesId) &&
            Objects.equals(churchId, that.churchId) &&
            Objects.equals(churchCommunitiesId, that.churchCommunitiesId) &&
            Objects.equals(memberRitesId, that.memberRitesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        firstName,
        lastName,
        middleName,
        gender,
        phoneNumber,
        email,
        dateOfBith,
        placeOfBith,
        maritalStatus,
        work,
        placeOfWork,
        isActive,
        isDeceased,
        deceasedDate,
        relativesId,
        churchId,
        churchCommunitiesId,
        memberRitesId
        );
    }

    @Override
    public String toString() {
        return "MemberCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (middleName != null ? "middleName=" + middleName + ", " : "") +
                (gender != null ? "gender=" + gender + ", " : "") +
                (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (dateOfBith != null ? "dateOfBith=" + dateOfBith + ", " : "") +
                (placeOfBith != null ? "placeOfBith=" + placeOfBith + ", " : "") +
                (maritalStatus != null ? "maritalStatus=" + maritalStatus + ", " : "") +
                (work != null ? "work=" + work + ", " : "") +
                (placeOfWork != null ? "placeOfWork=" + placeOfWork + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
                (isDeceased != null ? "isDeceased=" + isDeceased + ", " : "") +
                (deceasedDate != null ? "deceasedDate=" + deceasedDate + ", " : "") +
                (relativesId != null ? "relativesId=" + relativesId + ", " : "") +
                (churchId != null ? "churchId=" + churchId + ", " : "") +
                (churchCommunitiesId != null ? "churchCommunitiesId=" + churchCommunitiesId + ", " : "") +
                (memberRitesId != null ? "memberRitesId=" + memberRitesId + ", " : "") +
            "}";
    }

}
