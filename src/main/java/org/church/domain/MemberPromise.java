package org.church.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A MemberPromise.
 */
@Entity
@Table(name = "member_promise")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MemberPromise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "promise_date", nullable = false)
    private LocalDate promiseDate;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Column(name = "other_promise")
    private String otherPromise;

    @Column(name = "fulfillment_date")
    private LocalDate fulfillmentDate;

    @Column(name = "is_fulfilled")
    private Boolean isFulfilled;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("memberPromises")
    private Member member;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("memberPromises")
    private ChurchActivity churchActivity;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPromiseDate() {
        return promiseDate;
    }

    public MemberPromise promiseDate(LocalDate promiseDate) {
        this.promiseDate = promiseDate;
        return this;
    }

    public void setPromiseDate(LocalDate promiseDate) {
        this.promiseDate = promiseDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public MemberPromise amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOtherPromise() {
        return otherPromise;
    }

    public MemberPromise otherPromise(String otherPromise) {
        this.otherPromise = otherPromise;
        return this;
    }

    public void setOtherPromise(String otherPromise) {
        this.otherPromise = otherPromise;
    }

    public LocalDate getFulfillmentDate() {
        return fulfillmentDate;
    }

    public MemberPromise fulfillmentDate(LocalDate fulfillmentDate) {
        this.fulfillmentDate = fulfillmentDate;
        return this;
    }

    public void setFulfillmentDate(LocalDate fulfillmentDate) {
        this.fulfillmentDate = fulfillmentDate;
    }

    public Boolean isIsFulfilled() {
        return isFulfilled;
    }

    public MemberPromise isFulfilled(Boolean isFulfilled) {
        this.isFulfilled = isFulfilled;
        return this;
    }

    public void setIsFulfilled(Boolean isFulfilled) {
        this.isFulfilled = isFulfilled;
    }

    public Member getMember() {
        return member;
    }

    public MemberPromise member(Member member) {
        this.member = member;
        return this;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public ChurchActivity getChurchActivity() {
        return churchActivity;
    }

    public MemberPromise churchActivity(ChurchActivity churchActivity) {
        this.churchActivity = churchActivity;
        return this;
    }

    public void setChurchActivity(ChurchActivity churchActivity) {
        this.churchActivity = churchActivity;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberPromise)) {
            return false;
        }
        return id != null && id.equals(((MemberPromise) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MemberPromise{" +
            "id=" + getId() +
            ", promiseDate='" + getPromiseDate() + "'" +
            ", amount=" + getAmount() +
            ", otherPromise='" + getOtherPromise() + "'" +
            ", fulfillmentDate='" + getFulfillmentDate() + "'" +
            ", isFulfilled='" + isIsFulfilled() + "'" +
            "}";
    }
}
