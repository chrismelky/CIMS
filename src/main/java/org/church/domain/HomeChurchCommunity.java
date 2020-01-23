package org.church.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A HomeChurchCommunity.
 */
@Entity
@Table(name = "home_church_community")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HomeChurchCommunity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "number_of_house_hold")
    private Integer numberOfHouseHold;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("homeChurchCommunities")
    private Church church;

    @ManyToOne
    @JsonIgnoreProperties({
        "homeChurchCommunities",
        "church",
        "relatives",
        "churchCommunities",
        "dateOfBirth",
        "placeOfBirth",
        "memberRites",
        "work",
        "placeOfWork" ,
        "maritalStatus",
        "isDeceased",
        "deceasedDate"})
    private Member chairman;

    @ManyToOne
    @JsonIgnoreProperties({
        "homeChurchCommunities",
        "church",
        "relatives",
        "churchCommunities",
        "dateOfBirth",
        "placeOfBirth",
        "memberRites",
        "work",
        "placeOfWork" ,
        "maritalStatus",
        "isDeceased",
        "deceasedDate"})
    private Member secretary;

    @ManyToOne
    @JsonIgnoreProperties({
        "homeChurchCommunities",
        "church",
        "relatives",
        "churchCommunities",
        "dateOfBirth",
        "placeOfBirth",
        "memberRites",
        "work",
        "placeOfWork" ,
        "maritalStatus",
        "isDeceased",
        "deceasedDate"})
    private Member treasurer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public HomeChurchCommunity name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumberOfHouseHold() {
        return numberOfHouseHold;
    }

    public HomeChurchCommunity numberOfHouseHold(Integer numberOfHouseHold) {
        this.numberOfHouseHold = numberOfHouseHold;
        return this;
    }

    public void setNumberOfHouseHold(Integer numberOfHouseHold) {
        this.numberOfHouseHold = numberOfHouseHold;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public HomeChurchCommunity phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public HomeChurchCommunity address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Church getChurch() {
        return church;
    }

    public HomeChurchCommunity church(Church church) {
        this.church = church;
        return this;
    }

    public void setChurch(Church church) {
        this.church = church;
    }

    public Member getChairman() {
        return chairman;
    }

    public HomeChurchCommunity chairMan(Member member) {
        this.chairman = member;
        return this;
    }

    public void setChairman(Member member) {
        this.chairman = member;
    }

    public Member getSecretary() {
        return secretary;
    }

    public HomeChurchCommunity secreatry(Member member) {
        this.secretary = member;
        return this;
    }

    public void setSecretary(Member member) {
        this.secretary = member;
    }

    public Member getTreasurer() {
        return treasurer;
    }

    public HomeChurchCommunity treasurer(Member member) {
        this.treasurer = member;
        return this;
    }

    public void setTreasurer(Member member) {
        this.treasurer = member;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HomeChurchCommunity)) {
            return false;
        }
        return id != null && id.equals(((HomeChurchCommunity) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "HomeChurchCommunity{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", numberOfHouseHold=" + getNumberOfHouseHold() +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
