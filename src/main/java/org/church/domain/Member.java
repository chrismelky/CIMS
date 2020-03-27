package org.church.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.church.domain.enumeration.Gender;

import org.church.domain.enumeration.MaritalStatus;

/**
 * A Member.
 */
@Entity
@Table(name = "member")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "member_rn")
    private String memberRn;

    @Column(name = "church_rn")
    @Size(max = 20)
    private String churchRn;

    @NotNull
    @Size(max = 100)
    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @NotNull
    @Size(max = 100)
    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @Size(max = 100)
    @Column(name = "middle_name", length = 100)
    private String middleName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "place_of_birth")
    private String placeOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    @Column(name = "work")
    private String work;

    @Column(name = "place_of_work")
    private String placeOfWork;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_deceased")
    private Boolean isDeceased;

    @Column(name = "deceased_date")
    private LocalDate deceasedDate;

    @OneToMany(mappedBy = "member")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MemberRelative> relatives = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        {
            "members",
            "email",
            "phoneNumber",
            "address",
            "latitude",
            "longitude",
            "otherDetails",
            "parent",
            "type"
        })
    private Church church;

    @ManyToOne()
    @JsonIgnoreProperties({
        "church",
        "chairman",
        "secretary" ,
        "treasurer",
        "address",
        "numberOfHouseHold",
        "phoneNumber"
    })
    private HomeChurchCommunity homeChurchCommunity;

    public HomeChurchCommunity getHomeChurchCommunity() {
        return homeChurchCommunity;
    }

    public void setHomeChurchCommunity(HomeChurchCommunity homeChurchCommunity) {
        this.homeChurchCommunity = homeChurchCommunity;
    }

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "member_church_communities",
               joinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "church_communities_id", referencedColumnName = "id"))
    private Set<ChurchCommunity> churchCommunities = new HashSet<>();

    @OneToMany(mappedBy = "member")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MemberRite> memberRites = new HashSet<>();



    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberRn() {
        return memberRn;
    }

    public void setMemberRn(String memberRn) {
        this.memberRn = memberRn;
    }

    public String getFirstName() {
        return firstName;
    }

    public Member firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Member lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public Member middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Gender getGender() {
        return gender;
    }

    public Member gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Member phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public Member email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Member dateOfBith(LocalDate dateOfBith) {
        this.dateOfBirth = dateOfBith;
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public Member placeOfBith(String placeOfBith) {
        this.placeOfBirth = placeOfBith;
        return this;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public Member maritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
        return this;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getWork() {
        return work;
    }

    public Member work(String work) {
        this.work = work;
        return this;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getPlaceOfWork() {
        return placeOfWork;
    }

    public Member placeOfWork(String placeOfWork) {
        this.placeOfWork = placeOfWork;
        return this;
    }

    public void setPlaceOfWork(String placeOfWork) {
        this.placeOfWork = placeOfWork;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Member isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isIsDeceased() {
        return isDeceased;
    }

    public Member isDeceased(Boolean isDeceased) {
        this.isDeceased = isDeceased;
        return this;
    }

    public void setIsDeceased(Boolean isDeceased) {
        this.isDeceased = isDeceased;
    }

    public LocalDate getDeceasedDate() {
        return deceasedDate;
    }

    public Member deceasedDate(LocalDate deceasedDate) {
        this.deceasedDate = deceasedDate;
        return this;
    }

    public void setDeceasedDate(LocalDate deceasedDate) {
        this.deceasedDate = deceasedDate;
    }

    public Set<MemberRelative> getRelatives() {
        return relatives;
    }

    public Member relatives(Set<MemberRelative> memberRelatives) {
        this.relatives = memberRelatives;
        return this;
    }

    public Member addRelatives(MemberRelative memberRelative) {
        this.relatives.add(memberRelative);
        memberRelative.setMember(this);
        return this;
    }

    public Member removeRelatives(MemberRelative memberRelative) {
        this.relatives.remove(memberRelative);
        memberRelative.setMember(null);
        return this;
    }

    public void setRelatives(Set<MemberRelative> memberRelatives) {
        this.relatives = memberRelatives;
    }

    public Church getChurch() {
        return church;
    }

    public Member church(Church church) {
        this.church = church;
        return this;
    }

    public void setChurch(Church church) {
        this.church = church;
    }

    public Set<ChurchCommunity> getChurchCommunities() {
        return churchCommunities;
    }

    public Member churchCommunities(Set<ChurchCommunity> churchCommunities) {
        this.churchCommunities = churchCommunities;
        return this;
    }

    public Member addChurchCommunities(ChurchCommunity churchCommunity) {
        this.churchCommunities.add(churchCommunity);
        churchCommunity.getMembers().add(this);
        return this;
    }

    public Member removeChurchCommunities(ChurchCommunity churchCommunity) {
        this.churchCommunities.remove(churchCommunity);
        churchCommunity.getMembers().remove(this);
        return this;
    }

    public void setChurchCommunities(Set<ChurchCommunity> churchCommunities) {
        this.churchCommunities = churchCommunities;
    }

    public Set<MemberRite> getMemberRites() {
        return memberRites;
    }

    public Member memberRites(Set<MemberRite> memberRites) {
        this.memberRites = memberRites;
        return this;
    }

    public Member addMemberRites(MemberRite memberRite) {
        this.memberRites.add(memberRite);
        memberRite.setMember(this);
        return this;
    }

    public Member removeMemberRites(MemberRite memberRite) {
        this.memberRites.remove(memberRite);
        memberRite.setMember(null);
        return this;
    }

    public String getChurchRn() {
        return churchRn;
    }

    public void setChurchRn(String churchRn) {
        this.churchRn = churchRn;
    }

    public void setMemberRites(Set<MemberRite> memberRites) {
        this.memberRites = memberRites;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member)) {
            return false;
        }
        return id != null && id.equals(((Member) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Member{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", middleName='" + getMiddleName() + "'" +
            ", gender='" + getGender() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", email='" + getEmail() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", placeOfBirth='" + getPlaceOfBirth() + "'" +
            ", maritalStatus='" + getMaritalStatus() + "'" +
            ", work='" + getWork() + "'" +
            ", placeOfWork='" + getPlaceOfWork() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", isDeceased='" + isIsDeceased() + "'" +
            ", deceasedDate='" + getDeceasedDate() + "'" +
            "}";
    }
}
