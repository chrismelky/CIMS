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
 * Criteria class for the {@link org.church.domain.ChurchCommunity} entity. This class is used
 * in {@link org.church.web.rest.ChurchCommunityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /church-communities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ChurchCommunityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter churchId;

    private LongFilter chairPersonId;

    private LongFilter secretaryId;

    private LongFilter treasurerId;

    private LongFilter membersId;

    public ChurchCommunityCriteria(){
    }

    public ChurchCommunityCriteria(ChurchCommunityCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.churchId = other.churchId == null ? null : other.churchId.copy();
        this.chairPersonId = other.chairPersonId == null ? null : other.chairPersonId.copy();
        this.secretaryId = other.secretaryId == null ? null : other.secretaryId.copy();
        this.treasurerId = other.treasurerId == null ? null : other.treasurerId.copy();
        this.membersId = other.membersId == null ? null : other.membersId.copy();
    }

    @Override
    public ChurchCommunityCriteria copy() {
        return new ChurchCommunityCriteria(this);
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

    public LongFilter getChurchId() {
        return churchId;
    }

    public void setChurchId(LongFilter churchId) {
        this.churchId = churchId;
    }

    public LongFilter getChairPersonId() {
        return chairPersonId;
    }

    public void setChairPersonId(LongFilter chairPersonId) {
        this.chairPersonId = chairPersonId;
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

    public LongFilter getMembersId() {
        return membersId;
    }

    public void setMembersId(LongFilter membersId) {
        this.membersId = membersId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ChurchCommunityCriteria that = (ChurchCommunityCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(churchId, that.churchId) &&
            Objects.equals(chairPersonId, that.chairPersonId) &&
            Objects.equals(secretaryId, that.secretaryId) &&
            Objects.equals(treasurerId, that.treasurerId) &&
            Objects.equals(membersId, that.membersId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        churchId,
        chairPersonId,
        secretaryId,
        treasurerId,
        membersId
        );
    }

    @Override
    public String toString() {
        return "ChurchCommunityCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (churchId != null ? "churchId=" + churchId + ", " : "") +
                (chairPersonId != null ? "chairPersonId=" + chairPersonId + ", " : "") +
                (secretaryId != null ? "secretaryId=" + secretaryId + ", " : "") +
                (treasurerId != null ? "treasurerId=" + treasurerId + ", " : "") +
                (membersId != null ? "membersId=" + membersId + ", " : "") +
            "}";
    }

}
