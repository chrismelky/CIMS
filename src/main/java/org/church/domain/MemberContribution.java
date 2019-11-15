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
    private Member member;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("memberContributions")
    private Church church;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("memberContributions")
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JsonIgnoreProperties("memberContributions")
    private MemberPromise promise;

    @ManyToOne
    @JsonIgnoreProperties("memberContributions")
    private ContributionType type;

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

    public Member getMember() {
        return member;
    }

    public MemberContribution member(Member member) {
        this.member = member;
        return this;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Church getChurch() {
        return church;
    }

    public MemberContribution church(Church church) {
        this.church = church;
        return this;
    }

    public void setChurch(Church church) {
        this.church = church;
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

    public MemberPromise getPromise() {
        return promise;
    }

    public MemberContribution promise(MemberPromise memberPromise) {
        this.promise = memberPromise;
        return this;
    }

    public void setPromise(MemberPromise memberPromise) {
        this.promise = memberPromise;
    }

    public ContributionType getType() {
        return type;
    }

    public MemberContribution type(ContributionType contributionType) {
        this.type = contributionType;
        return this;
    }

    public void setType(ContributionType contributionType) {
        this.type = contributionType;
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
