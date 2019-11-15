package org.church.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ChurchCommunity.
 */
@Entity
@Table(name = "church_community")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ChurchCommunity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("churchCommunities")
    private Church church;

    @ManyToOne
    @JsonIgnoreProperties("churchCommunities")
    private Member chairPerson;

    @ManyToOne
    @JsonIgnoreProperties("churchCommunities")
    private Member secretary;

    @ManyToOne
    @JsonIgnoreProperties("churchCommunities")
    private Member treasurer;

    @ManyToMany(mappedBy = "churchCommunities")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Member> members = new HashSet<>();

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

    public ChurchCommunity name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Church getChurch() {
        return church;
    }

    public ChurchCommunity church(Church church) {
        this.church = church;
        return this;
    }

    public void setChurch(Church church) {
        this.church = church;
    }

    public Member getChairPerson() {
        return chairPerson;
    }

    public ChurchCommunity chairPerson(Member member) {
        this.chairPerson = member;
        return this;
    }

    public void setChairPerson(Member member) {
        this.chairPerson = member;
    }

    public Member getSecretary() {
        return secretary;
    }

    public ChurchCommunity secretary(Member member) {
        this.secretary = member;
        return this;
    }

    public void setSecretary(Member member) {
        this.secretary = member;
    }

    public Member getTreasurer() {
        return treasurer;
    }

    public ChurchCommunity treasurer(Member member) {
        this.treasurer = member;
        return this;
    }

    public void setTreasurer(Member member) {
        this.treasurer = member;
    }

    public Set<Member> getMembers() {
        return members;
    }

    public ChurchCommunity members(Set<Member> members) {
        this.members = members;
        return this;
    }

    public ChurchCommunity addMembers(Member member) {
        this.members.add(member);
        member.getChurchCommunities().add(this);
        return this;
    }

    public ChurchCommunity removeMembers(Member member) {
        this.members.remove(member);
        member.getChurchCommunities().remove(this);
        return this;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChurchCommunity)) {
            return false;
        }
        return id != null && id.equals(((ChurchCommunity) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ChurchCommunity{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
