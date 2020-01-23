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
 * A PeriodContributionItem.
 */
@Entity
@Table(name = "period_contribution_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PeriodContributionItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "date_received", nullable = false)
    private LocalDate dateReceived;

    @NotNull
    @Column(name = "received_by", nullable = false)
    private String receivedBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("periodContributionItems")
    private PeriodContribution periodContribution;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PeriodContributionItem amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public PeriodContributionItem description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateReceived() {
        return dateReceived;
    }

    public PeriodContributionItem dateReceived(LocalDate dateReceived) {
        this.dateReceived = dateReceived;
        return this;
    }

    public void setDateReceived(LocalDate dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public PeriodContributionItem receivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
        return this;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public PeriodContribution getPeriodContribution() {
        return periodContribution;
    }

    public PeriodContributionItem periodContribution(PeriodContribution periodContribution) {
        this.periodContribution = periodContribution;
        return this;
    }

    public void setPeriodContribution(PeriodContribution periodContribution) {
        this.periodContribution = periodContribution;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PeriodContributionItem)) {
            return false;
        }
        return id != null && id.equals(((PeriodContributionItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PeriodContributionItem{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", description='" + getDescription() + "'" +
            ", dateReceived='" + getDateReceived() + "'" +
            ", receivedBy='" + getReceivedBy() + "'" +
            "}";
    }
}
