package org.church.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link org.church.domain.HomeChurchCommunity} entity. This class is used
 * in {@link org.church.web.rest.HomeChurchCommunityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /home-church-communities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HomeChurchCommunityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter numberOfHouseHold;

    private StringFilter phoneNumber;

    private StringFilter address;

    private LongFilter churchId;

    private LongFilter chairmanId;

    private LongFilter secretaryId;

    private LongFilter treasurerId;

    public HomeChurchCommunityCriteria(){
    }

    public HomeChurchCommunityCriteria(HomeChurchCommunityCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.numberOfHouseHold = other.numberOfHouseHold == null ? null : other.numberOfHouseHold.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.churchId = other.churchId == null ? null : other.churchId.copy();
        this.chairmanId = other.chairmanId == null ? null : other.chairmanId.copy();
        this.secretaryId = other.secretaryId == null ? null : other.secretaryId.copy();
        this.treasurerId = other.treasurerId == null ? null : other.treasurerId.copy();
    }

    @Override
    public HomeChurchCommunityCriteria copy() {
        return new HomeChurchCommunityCriteria(this);
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

    public IntegerFilter getNumberOfHouseHold() {
        return numberOfHouseHold;
    }

    public void setNumberOfHouseHold(IntegerFilter numberOfHouseHold) {
        this.numberOfHouseHold = numberOfHouseHold;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public LongFilter getChurchId() {
        return churchId;
    }

    public void setChurchId(LongFilter churchId) {
        this.churchId = churchId;
    }

    public LongFilter getChairmanId() {
        return chairmanId;
    }

    public void setChairmanId(LongFilter chairmanId) {
        this.chairmanId = chairmanId;
    }

    public LongFilter getSecretaryId() {
        return secretaryId;
    }

    public void setSecretaryId(LongFilter secretaryId) {
        this.secretaryId = secretaryId;
    }

    public LongFilter getTreasurerId() {
        return treasurerId;
    }

    public void setTreasurerId(LongFilter treasurerId) {
        this.treasurerId = treasurerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HomeChurchCommunityCriteria that = (HomeChurchCommunityCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(numberOfHouseHold, that.numberOfHouseHold) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(address, that.address) &&
            Objects.equals(churchId, that.churchId) &&
            Objects.equals(chairmanId, that.chairmanId) &&
            Objects.equals(secretaryId, that.secretaryId) &&
            Objects.equals(treasurerId, that.treasurerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        numberOfHouseHold,
        phoneNumber,
        address,
        churchId,
            chairmanId,
            secretaryId,
        treasurerId
        );
    }

    @Override
    public String toString() {
        return "HomeChurchCommunityCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (numberOfHouseHold != null ? "numberOfHouseHold=" + numberOfHouseHold + ", " : "") +
                (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (churchId != null ? "churchId=" + churchId + ", " : "") +
                (chairmanId != null ? "chairmanId=" + chairmanId + ", " : "") +
                (secretaryId != null ? "secretaryId=" + secretaryId + ", " : "") +
                (treasurerId != null ? "treasurerId=" + treasurerId + ", " : "") +
            "}";
    }

}
