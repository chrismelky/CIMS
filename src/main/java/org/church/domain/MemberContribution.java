package org.church.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A MemberContribution.
 */
@Entity
@Table(name = "member_contribution")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MemberContribution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("memberContributions")
    private PaymentMethod paymentMethod;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("memberContributions")
    private MemberPromise memberPromise;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public MemberContribution paymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public MemberContribution amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public MemberContribution paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public MemberPromise getMemberPromise() {
        return memberPromise;
    }

    public void setMemberPromise(MemberPromise memberPromise) {
        this.memberPromise = memberPromise;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberContribution)) {
            return false;
        }
        return id != null && id.equals(((MemberContribution) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MemberContribution{" +
            "id=" + getId() +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", amount=" + getAmount() +
            "}";
    }
}
